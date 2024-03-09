package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.Security.SecurityConfig;
import examination.teacherAndStudents.dto.SubjectScheduleTeacherUpdateDto;
import examination.teacherAndStudents.dto.TeacherAttendanceRequest;
import examination.teacherAndStudents.entity.*;
import examination.teacherAndStudents.error_handler.*;
import examination.teacherAndStudents.repository.*;
import examination.teacherAndStudents.service.TeacherAttendanceService;
import examination.teacherAndStudents.utils.AttendanceStatus;
import examination.teacherAndStudents.utils.Roles;
import examination.teacherAndStudents.utils.StudentTerm;
import examination.teacherAndStudents.utils.TeachingStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherAttendanceServiceImpl implements TeacherAttendanceService {
    @Autowired
    private TeacherAttendanceRepository teacherAttendanceRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubjectScheduleRepository subjectScheduleRepository;
    @Autowired
    private TimetableRepository timetableRepository;
    @Autowired
    private AttendancePercentRepository attendancePercentRepository;
    @Autowired
    private TeacherAttendancePercentRepository teacherAttendancePercentRepository;


    public void checkIn(String location) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User teacher = userRepository.findByEmailAndRoles(email, Roles.TEACHER);

            if (teacher == null) {
                throw new CustomNotFoundException("Please log in as a teacher."); // Improved error message
            }

            LocalDateTime checkInTime = LocalDateTime.now();

            // Check if the teacher has already checked in for the day
            TeacherAttendance existingAttendance = teacherAttendanceRepository
                    .findFirstByUserAndCheckInTimeBetweenOrderByCheckInTimeDesc(
                            teacher,
                            LocalDate.now().atStartOfDay(),
                            LocalDate.now().atTime(23, 59, 59)
                    )
                    .orElse(null);

            if (existingAttendance != null && existingAttendance.getCheckOutTime() == null) {
                throw new RuntimeException("Teacher has already checked in for the day.");
            }

            TeacherAttendance attendance = new TeacherAttendance();
            attendance.setTeacherId(teacher.getId());
            attendance.setCheckInTime(checkInTime);
            attendance.setCheckInLocation(location);
            attendance.setUser(teacher);
            attendance.setStatus(AttendanceStatus.PRESENT);
            teacherAttendanceRepository.save(attendance);
        } catch (Exception e) {
            // Handle exceptions appropriately (log, rethrow, etc.)
            throw new RuntimeException("Error during check-in process.", e);
        }
    }

    public void checkOut(String location) {
        List<String> errors = new ArrayList<>();

        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User teacher = userRepository.findByEmailAndRoles(email, Roles.TEACHER);

            if (teacher == null) {
                errors.add("Please log in as a teacher.");
            }

            LocalDateTime checkOutTime = LocalDateTime.now();

            // Find the latest attendance record for the teacher
            TeacherAttendance attendance = teacherAttendanceRepository
                    .findFirstByUserOrderByCheckInTimeDesc(teacher)
                    .orElseThrow(() -> new RuntimeException("Teacher has not checked in."));

            // Check if the teacher has already checked out
            if (attendance.getCheckOutTime() != null) {
                errors.add("Teacher has already checked out.");
            }

            // Check if the teacher has checked in before allowing check-out
            if (attendance.getCheckInTime() == null) {
                errors.add("Teacher has not checked in.");
            }

            if (!errors.isEmpty()) {
                throw new RuntimeException(String.join(" ", errors));
            }

            attendance.setCheckOutTime(checkOutTime);
            attendance.setCheckOutLocation(location);

            teacherAttendanceRepository.save(attendance);
        } catch (Exception e) {
            errors.add("Error during check-out process.");
            // Log the exception or handle it based on your requirements
        } finally {
            if (!errors.isEmpty()) {
                throw new RuntimeException(String.join(" ", errors));
            }
        }
    }




    @Override
    public List<TeacherAttendance> getAllTeacherAttendance() {
        try {
            return teacherAttendanceRepository.findAll();
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while fetching all teacher attendance: " + e.getMessage());
        }
    }

    @Override
    public List<TeacherAttendance> getTeacherAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            if (startDate == null || endDate == null) {
                throw new IllegalArgumentException("Start date and end date cannot be null");
            }
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date must be earlier than end date");
            }
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

            return teacherAttendanceRepository.findByCheckInTimeBetween(startDateTime, endDateTime);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error occurred: " + e.getMessage());
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while fetching teacher attendance: " + e.getMessage());
        }
    }


    public List<TeacherAttendance> getTeacherAttendanceByTeacherAndDateRange(
            Long teacherId,
            LocalDate startDate,
            LocalDate endDate) {
        try {
            if (teacherId == null || startDate == null || endDate == null) {
                throw new IllegalArgumentException("Teacher ID, start date, and end date cannot be null");
            }
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date must be earlier than end date");
            }
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            // Fetch teacher by ID
            User teacher = userRepository.findByIdAndRoles(teacherId, Roles.TEACHER);
            if (teacher == null) {
                throw new EntityNotFoundException("Teacher not found with ID: " + teacherId);
            }
            // Fetch teacher attendance records
            return teacherAttendanceRepository.findByUserIdAndCheckInTimeBetween(teacherId, startDateTime, endDateTime);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error occurred: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Error occurred: " + e.getMessage());
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while fetching teacher attendance: " + e.getMessage());
        }



    }
    


    @Transactional
    public SubjectSchedule updateTeachingStatus(SubjectScheduleTeacherUpdateDto updateDto) {
        try {
            // Retrieve the subject schedule by ID
            SubjectSchedule subjectSchedule = subjectScheduleRepository.findByIdAndTimetableDayOfWeek(updateDto.getScheduleId(), updateDto.getDayOfWeek());
            if(subjectSchedule == null){
                throw  new CustomNotFoundException("Ensure the time table day match the schedule for the day");
            }
            Timetable timetable = timetableRepository.findBySubjectSchedules(subjectSchedule);

           //  Check if the class has already been taught
            if (subjectSchedule.getTeachingStatus() == TeachingStatus.TAUGHT) {
                throw new CustomInternalServerException("This class has already been taught.");
            }
            // Update the topic and teaching status
            subjectSchedule.setTopic(updateDto.getTopic());
            subjectSchedule.setSubject(subjectSchedule.getSubject());
            subjectSchedule.setStartTime(subjectSchedule.getStartTime());
            subjectSchedule.setEndTime(subjectSchedule.getEndTime());
            subjectSchedule.setTimetable(timetable);
            subjectSchedule.setTeachingStatus(TeachingStatus.TAUGHT);
            subjectSchedule.setTeachersUpdatedTime(LocalDateTime.now());

            // Save the updated subject schedule
            return subjectScheduleRepository.save(subjectSchedule);
        } catch (Exception e) {
            // Handle or log the exception as needed
            throw new CustomInternalServerException("An error occurred while updating teacher taught topic: " + e.getMessage());
        }
    }


    @Transactional
    public List<SubjectSchedule> getAllNotTaughtSubjectSchedules() {
        return subjectScheduleRepository.findAllByTeachingStatus(TeachingStatus.TAUGHT);
    }

    @Transactional
    public List<SubjectSchedule> getAllTaughtSubjectSchedules() {
        return subjectScheduleRepository.findAllByTeachingStatus(TeachingStatus.NOT_TAUGHT);
    }



}
