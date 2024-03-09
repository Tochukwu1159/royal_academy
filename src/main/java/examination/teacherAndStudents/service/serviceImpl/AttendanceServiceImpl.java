package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.dto.AttendanceRequest;
import examination.teacherAndStudents.entity.*;
import examination.teacherAndStudents.error_handler.AttendanceAlreadyTakenException;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.repository.*;
import examination.teacherAndStudents.service.AttendanceService;
import examination.teacherAndStudents.utils.AttendanceStatus;
import examination.teacherAndStudents.utils.StudentTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private AttendancePercentRepository attendancePercentRepository;
    @Autowired
    private SubClassRepository subClassRepository;

    public void takeAttendance(AttendanceRequest attendanceRequest) {
        try {
            // Retrieve the student from the database
            User student = userRepository.findById(attendanceRequest.getStudentId())
                    .orElseThrow(() -> new CustomNotFoundException("Student not found with ID: " + attendanceRequest.getStudentId()));
            SubClass studentClass = subClassRepository.findById(attendanceRequest.getClassLevelId())
                    .orElseThrow(() -> new CustomNotFoundException("Class not found with ID: " + attendanceRequest.getClassLevelId()));


            // Check if attendance for the given date and student already exists
            Attendance existingAttendance = attendanceRepository.findByUserAndDate(student, attendanceRequest.getDate());


            if (existingAttendance != null) {
                // Attendance for the given date already exists, throw a custom exception
                throw new AttendanceAlreadyTakenException("Attendance for date " + attendanceRequest.getDate() + " already taken for student ID: " + student.getId());
        } else {
                // Create a new attendance entry
                Attendance attendance = new Attendance();
                attendance.setUser(student);
                attendance.setTerm(attendanceRequest.getStudentTerm());
                attendance.setYear(attendanceRequest.getYear());
                attendance.setSubClass(studentClass);
                attendance.setDate(attendanceRequest.getDate());
                attendance.setStatus(attendanceRequest.getStatus());
                attendanceRepository.save(attendance);
            }

            // After recording attendance, update the attendance percentage
            calculateAttendancePercentage(student.getId(), studentClass.getId(), attendanceRequest.getStudentTerm());

        } catch (CustomNotFoundException e) {
            // Handle not found exception
            throw new CustomNotFoundException("Error: " + e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            throw new CustomInternalServerException("Error taking attendance: " + e.getMessage());
        }
    }



    public List<Attendance> getStudentAttendance(Long studentId, LocalDate startDate, LocalDate endDate) {
        try {
            // Retrieve the student from the database
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new CustomNotFoundException("Student not found with ID: " + studentId));

            // Retrieve attendance records for the student within the specified date range
            return attendanceRepository.findByUserAndDateBetween(student, startDate, endDate);
        } catch (CustomNotFoundException e) {
            // Handle custom not found exception
            throw e;
        } catch (Exception e) {
            // Handle other exceptions
            throw new CustomInternalServerException("Error getting student attendance: " + e.getMessage());
        }
    }

    public List<Attendance> getAllStudentsAttendance(Long studentId, LocalDate startDate, LocalDate endDate) {
        try {
            // Retrieve the student from the database
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new CustomNotFoundException("Student not found with ID: " + studentId));

            // Retrieve attendance records for the student within the specified date range
            return attendanceRepository.findByUserAndDateBetween(student, startDate, endDate);
        } catch (CustomNotFoundException e) {
            // Handle custom not found exception
            throw e;
        } catch (Exception e) {
            // Handle other exceptions
            throw new CustomInternalServerException("Error getting student attendance: " + e.getMessage());
        }
    }

//    public List<Attendance> getStudentAttendanceByClass(Long classId, LocalDate startDate, LocalDate endDate) {
//        try {
//            List<User> studentsInClass = userRepository.findByClassLevelId(classId);
//            return attendanceRepository.findByUserInAndDateBetween(studentsInClass, startDate, endDate);
//        } catch (Exception e) {
//            // Handle exceptions
//            throw new CustomInternalServerException("Error getting student attendance by class: " + e.getMessage());
//        }
//    }

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




    // Add additional methods as needed, such as getting attendance for a specific date or class.
}

