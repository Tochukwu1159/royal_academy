package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.dto.*;
import examination.teacherAndStudents.entity.*;
import examination.teacherAndStudents.error_handler.AttendanceAlreadyTakenException;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.error_handler.SubscriptionExpiredException;
import examination.teacherAndStudents.repository.*;
import examination.teacherAndStudents.service.AttendanceService;
import examination.teacherAndStudents.service.SchoolService;
import examination.teacherAndStudents.utils.AttendanceStatus;
import examination.teacherAndStudents.utils.StudentTerm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {


    private final UserRepository userRepository;

    private final SchoolService schoolService;


    private final AttendanceRepository attendanceRepository;

    private final AttendancePercentRepository attendancePercentRepository;

    private final  SubClassRepository subClassRepository;

    private final AcademicYearRepository academicYearRepository;

    private final ClassCategoryRepository studentClassLevelRepository;

    private final ModelMapper modelMapper;
    private final ProfileRepository profileRepository;


    @Override
    public List<StudentListResponse>  loadStudentListPerSubClass(StudentListRequest studentListRequest) {
        try {
            AcademicYear studentAcademicYear = academicYearRepository.findById(studentListRequest.getAcademicYearId())
                    .orElseThrow(() -> new CustomNotFoundException("Student academic year not found"));

            ClassCategory studentClassLevel = studentClassLevelRepository.findByIdAndAcademicYear(studentListRequest.getClassLevelId(), studentAcademicYear);

            SubClass studentClass = subClassRepository.findByIdAndClassCategory(studentListRequest.getSubClassId(),studentClassLevel);

            List<Profile> studentProfiles = profileRepository.findAllByStudentClass(studentClass);

            Long subClassId = studentClass.getId();

            return studentProfiles.stream()
                    .map(profile -> {
                        StudentListResponse response = modelMapper.map(profile, StudentListResponse.class);
                        response.setStudentClassId(subClassId);
                        return response;
                    })
                    .collect(Collectors.toList());

        } catch (CustomNotFoundException e) {
            // Handle not found exception
            throw new CustomNotFoundException("Error: " + e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            throw new CustomInternalServerException("Error taking attendance: " + e.getMessage());
        }
    }



@Override
    public void takeAttendance(AttendanceRequest attendanceRequest) {

        try {
                        User student = userRepository.findById(attendanceRequest.getStudentUniqueId())
                    .orElseThrow(() -> new CustomNotFoundException("Student  not found"));

            SubClass studentClass = subClassRepository.findById(attendanceRequest.getStudentClassId())
                    .orElseThrow(() -> new CustomNotFoundException("Student academic year not found"));

                    Attendance attendance = new Attendance();
                attendance.setUser(student);
                attendance.setSubClass(studentClass);
                attendance.setDate(LocalDate.now());
                attendance.setStatus(attendanceRequest.getStatus());

            // Save the attendance records in bulk
            attendanceRepository.save(attendance);

        } catch (CustomNotFoundException e) {
            // Handle not found exception
            throw new CustomNotFoundException("Error: " + e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            throw new CustomInternalServerException("Error taking attendance: " + e.getMessage());
        }
    }


    @Override

    public AttendanceResponse getStudentAttendance(Long studentId, LocalDate startDate, LocalDate endDate) {
        try {
            // Retrieve the student from the database
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new CustomNotFoundException("Student not found with ID: " + studentId));

            // Retrieve all attendance records for the student within the specified date range
            List<Attendance> attendanceRecords = attendanceRepository.findByUserAndDateBetween(student, startDate, endDate);

            // Calculate number of days present and absent
            long daysPresent = attendanceRecords.stream()
                    .filter(attendance -> attendance.getStatus() == AttendanceStatus.PRESENT)
                    .count();
            long daysAbsent = attendanceRecords.size() - daysPresent;

            // Calculate percentage attendance
            double totalAttendanceDays = ChronoUnit.DAYS.between(startDate, endDate) + 1; // Adding 1 to include endDate
            double percentageAttendance = (daysPresent / totalAttendanceDays) * 100;

            Double roundedPercentage = (double) Math.round(percentageAttendance);

            // Group attendance records by day of the week
            Map<DayOfWeek, List<Attendance>> attendanceByDayOfWeek = attendanceRecords.stream()
                    .collect(Collectors.groupingBy(attendance -> attendance.getDate().getDayOfWeek()));

            // Construct the AttendanceResponse
            AttendanceResponse response = new AttendanceResponse();
            response.setStudentName(student.getFirstName() + student.getLastName());
            response.setStudentId(studentId);
            response.setDaysPresent(daysPresent);
            response.setDaysAbsent(daysAbsent);
            response.setPercentageAttendance(roundedPercentage);
            response.setAttendanceByDayOfWeek(attendanceByDayOfWeek);

            return response;
        } catch (CustomNotFoundException e) {
            // Handle custom not found exception
            throw e;
        } catch (Exception e) {
            // Handle other exceptions
            throw new CustomInternalServerException("Error getting student attendance: " + e.getMessage());
        }
    }
//    @Override
//    public List<Attendance> getAllStudentsAttendance(Long studentId, LocalDate startDate, LocalDate endDate) {
//        try {
//            // Retrieve the student from the database
//            User student = userRepository.findById(studentId)
//                    .orElseThrow(() -> new CustomNotFoundException("Student not found with ID: " + studentId));
//
//            // Retrieve attendance records for the student within the specified date range
//            return attendanceRepository.findByUserAndDateBetween(student, startDate, endDate);
//        } catch (CustomNotFoundException e) {
//            // Handle custom not found exception
//            throw e;
//        } catch (Exception e) {
//            // Handle other exceptions
//            throw new CustomInternalServerException("Error getting student attendance: " + e.getMessage());
//        }
//    }

//    public List<Attendance> getStudentAttendanceByClass(Long classId, LocalDate startDate, LocalDate endDate) {
//        try {
//            List<User> studentsInClass = userRepository.findByClassLevelId(classId);
//            return attendanceRepository.findByUserInAndDateBetween(studentsInClass, startDate, endDate);
//        } catch (Exception e) {
//            // Handle exceptions
//            throw new CustomInternalServerException("Error getting student attendance by class: " + e.getMessage());
//        }
//    }
@Override
    public double calculateAttendancePercentage(Long userId, Long classLevelId, StudentTerm studentTerm) {
        try {
            Optional<User> optionalStudent = userRepository.findById(userId);

            if (optionalStudent.isEmpty()) {
                throw new CustomNotFoundException("Student not found");
            }

            SubClass classLevel = subClassRepository.findById(classLevelId)
                    .orElseThrow(() -> new CustomNotFoundException("Class Level not found with ID: " + classLevelId));

            User student = optionalStudent.get();

            // Check if the attendance percentage already exists
            Optional<AttendancePercent> existingAttendancePercent = attendancePercentRepository.findByUserAndStudentTerm(student, studentTerm);

            // Get the total number of attendance records for the user
            long totalAttendanceRecords = attendanceRepository.countByUserIdAndAndTerm(userId, studentTerm);

            // Get the number of days the user attended
            long daysAttended = attendanceRepository.countByUserIdAndAndTermAndAndStatus(userId, studentTerm, AttendanceStatus.PRESENT);

            // Check if totalAttendanceRecords is zero to avoid division by zero
            if (totalAttendanceRecords == 0) {
                throw new CustomInternalServerException("Total attendance records are zero. Cannot calculate percentage.");
            }

            // Calculate the attendance percentage
            double attendancePercentage = (double) daysAttended / totalAttendanceRecords * 100;

            // Round the attendance percentage to the nearest whole number
            Double roundedPercentage = (double) Math.round(attendancePercentage);

            // Save the attendance percentage in the AttendancePercent entity
            AttendancePercent attendancePercent = existingAttendancePercent.orElse(new AttendancePercent());

            attendancePercent.setAttendancePercentage(roundedPercentage);
            attendancePercent.setStudentTerm(studentTerm);
            attendancePercent.setUser(student);
            attendancePercent.setSubClass(classLevel);

            attendancePercentRepository.save(attendancePercent);

            return roundedPercentage;
        } catch (CustomNotFoundException e) {
            throw new CustomNotFoundException("An error occurred: " + e.getMessage());
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while calculating attendance percentage: " + e.getMessage());
        }
    }

    private boolean isSubscriptionExpired(School school) {
        LocalDate expiryDate = school.getSubscriptionExpiryDate();
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }




    // Add additional methods as needed, such as getting attendance for a specific date or class.
}

