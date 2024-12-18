package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.AttendancePercentageRequest;
import examination.teacherAndStudents.dto.AttendanceRequest;
import examination.teacherAndStudents.dto.AttendanceResponse;
import examination.teacherAndStudents.entity.Attendance;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/take")
    public ResponseEntity<String> takeAttendance(@RequestBody AttendanceRequest attendanceRequest) {
        try {
            // Assuming you have a method in the service to handle attendance
            attendanceService.takeAttendance(attendanceRequest);
            return ResponseEntity.ok("Attendance taken successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error taking attendance: " + e.getMessage());
        }
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<AttendanceResponse> getStudentAttendance(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            // Retrieve the student from the database
            return new ResponseEntity<>(attendanceService.getStudentAttendance(studentId,startDate, endDate), HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/calculate-attendance-percentage")
    public ResponseEntity<Double> calculateAttendancePercentage(@RequestBody AttendancePercentageRequest attendancePercentageRequest) {
        try {
            double attendancePercentage = attendanceService.calculateAttendancePercentage(attendancePercentageRequest.getUserId(), attendancePercentageRequest.getClassLevelId(), attendancePercentageRequest.getStudentTerm());
            return ResponseEntity.ok(attendancePercentage);
        } catch (CustomNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


//    @GetMapping("/students-by-class")
//    public ResponseEntity<List<Attendance>> getStudentAttendanceByClass(
//            @RequestParam Long classId,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//        List<Attendance> studentAttendanceList = attendanceService.getStudentAttendanceByClass(classId, startDate, endDate);
//        return ResponseEntity.ok(studentAttendanceList);
//    }

    // Add more methods for fetching attendance, generating reports, etc.
}
