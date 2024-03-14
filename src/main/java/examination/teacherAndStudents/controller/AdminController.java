package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.SickLeaveDTO;
import examination.teacherAndStudents.dto.SubjectScheduleTeacherUpdateDto;
import examination.teacherAndStudents.entity.StaffAttendance;
import examination.teacherAndStudents.entity.SubjectSchedule;
import examination.teacherAndStudents.entity.StaffAttendance;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.service.AdminService;
import examination.teacherAndStudents.service.StaffAttendanceService;
import examination.teacherAndStudents.utils.AccountUtils;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class AdminController {

    private final AdminService userService;

    private final StaffAttendanceService teacherAttendanceService;

    public AdminController(AdminService userService, StaffAttendanceService teacherAttendanceService) {
        this.userService = userService;
        this.teacherAttendanceService = teacherAttendanceService;
    }

    @GetMapping("/students")
    public ResponseEntity<Page<User>> getAllStudents(@RequestParam(defaultValue = AccountUtils.PAGENO) Integer pageNo,
                                               @RequestParam(defaultValue = AccountUtils.PAGESIZE) Integer pageSize,
                                               @RequestParam(defaultValue = "id") String sortBy) {
        try {
            Page<User> studentsList = userService.getAllStudents(pageNo, pageSize, sortBy);
            return ResponseEntity.ok(studentsList);
        } catch (CustomNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (CustomInternalServerException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/teachers")
        public ResponseEntity<Page<User>> getAllTeachers(@RequestParam(defaultValue = AccountUtils.PAGENO) Integer pageNo,
                @RequestParam(defaultValue = AccountUtils.PAGESIZE) Integer pageSize,
                @RequestParam(defaultValue = "id") String sortBy) {
        try {
            Page<User> teachersList = userService.getAllStudents(pageNo, pageSize, sortBy);
            return ResponseEntity.ok(teachersList);
        } catch (CustomNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // You can customize the response as needed
        } catch (CustomInternalServerException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // You can customize the response as needed
        }
    }


    @PostMapping("/check-in")
    public ResponseEntity<String> checkIn(@RequestParam String location) {
        try {
            teacherAttendanceService.checkIn(location);
            return ResponseEntity.ok("Check-in successful");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/check-out")
    public ResponseEntity<String> checkOut(@RequestParam String location) {
        try {
            teacherAttendanceService.checkOut(location);
            return ResponseEntity.ok("Check-out successful");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @GetMapping("/calculate-teacher-percentage/{userId}/{term}")
//    public ResponseEntity<Double> calculateAttendancePercentage(@PathVariable Long userId,@PathVariable StudentTerm term ) {
//        try {
//            double attendancePercentage = teacherAttendanceService.calculateAttendancePercentage(userId, term);
//            return ResponseEntity.ok(attendancePercentage);
//        } catch (CustomNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

    @GetMapping("/teacher-attendance")
    public ResponseEntity<Page<StaffAttendance>> getAllStaffAttendance(@RequestParam(defaultValue = AccountUtils.PAGENO) Integer pageNo,
                                                                         @RequestParam(defaultValue = AccountUtils.PAGESIZE) Integer pageSize,
                                                                         @RequestParam(defaultValue = "id") String sortBy) {
            Page<StaffAttendance> attendanceList = teacherAttendanceService.getAllStaffAttendance(pageNo, pageSize, sortBy);
            return ResponseEntity.ok(attendanceList);
    }


    @GetMapping("/teacher-attendance/search")
    public ResponseEntity<List<StaffAttendance>> getStaffAttendanceByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<StaffAttendance> attendanceList = teacherAttendanceService.getStaffAttendanceByDateRange(startDate, endDate);
        return ResponseEntity.ok(attendanceList);
    }

//    @GetMapping("/teacher-attendance/search")
//    public ResponseEntity<Page<StaffAttendance>> getStaffAttendanceByDateRange(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//            @RequestParam(defaultValue = "0") int pageNo,
//            @RequestParam(defaultValue = "10") int pageSize,
//            @RequestParam(defaultValue = "id") String sortBy) {
//
//        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
//        Page<StaffAttendance> attendancePage = teacherAttendanceService.getStaffAttendanceByDateRange(startDate, endDate, paging);
//        return ResponseEntity.ok(attendancePage);
//    }

    @GetMapping("/search/teacher/date")
    public ResponseEntity<List<StaffAttendance>> getStaffAttendanceByTeacherAndDateRange(
            @RequestParam Long teacherId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<StaffAttendance> attendanceList = teacherAttendanceService.getStaffAttendanceByStaffAndDateRange(teacherId, startDate, endDate);
        return ResponseEntity.ok(attendanceList);

    }
    @PutMapping("/update-teaching-status")
    public ResponseEntity<SubjectSchedule> updateTeachingStatus(
            @RequestBody SubjectScheduleTeacherUpdateDto updateDto
          ) {
        SubjectSchedule updatedSchedule = teacherAttendanceService.updateStaffStatus(updateDto);
        return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
    }



    @GetMapping("/all")
    public ResponseEntity<Page<SickLeaveDTO>> getAllSickLeaves(@RequestParam(defaultValue = AccountUtils.PAGENO) Integer pageNo,
                                                     @RequestParam(defaultValue = AccountUtils.PAGESIZE) Integer pageSize,
                                                     @RequestParam(defaultValue = "id") String sortBy) {
            Page<SickLeaveDTO> allSickLeaves = userService.getAllSickLeaves(pageNo, pageSize, sortBy);
            return ResponseEntity.ok(allSickLeaves);
    }

    @PostMapping("/approve/{sickLeaveId}")
    public ResponseEntity<Void> approveSickLeave(@PathVariable Long sickLeaveId) {
        userService.approveSickLeave(sickLeaveId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject/{sickLeaveId}")
    public ResponseEntity<Void> rejectSickLeave(@PathVariable Long sickLeaveId) {
        userService.rejectSickLeave(sickLeaveId);
        return ResponseEntity.ok().build();
    }
}