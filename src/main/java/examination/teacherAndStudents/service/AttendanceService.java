package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.AttendanceRequest;
import examination.teacherAndStudents.dto.AttendanceResponse;
import examination.teacherAndStudents.dto.StudentListRequest;
import examination.teacherAndStudents.dto.StudentListResponse;
import examination.teacherAndStudents.entity.Attendance;
import examination.teacherAndStudents.utils.StudentTerm;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    void takeAttendance(AttendanceRequest attendanceRequest);
    List<StudentListResponse>  loadStudentListPerSubClass(StudentListRequest studentListRequest);
    AttendanceResponse getStudentAttendance(Long studentId, LocalDate startDate, LocalDate endDate);
//    List<Attendance> getStudentAttendanceByClass(Long classId, LocalDate startDate, LocalDate endDate);
    double calculateAttendancePercentage(Long userId, Long classLevelId, StudentTerm studentTerm);
}
