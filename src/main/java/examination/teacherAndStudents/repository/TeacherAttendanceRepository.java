package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.Attendance;
import examination.teacherAndStudents.entity.AttendancePercent;
import examination.teacherAndStudents.entity.TeacherAttendance;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.utils.AttendanceStatus;
import examination.teacherAndStudents.utils.StudentTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TeacherAttendanceRepository extends JpaRepository<TeacherAttendance, Long> {

    List<TeacherAttendance> findByCheckInTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<TeacherAttendance> findByUserIdAndCheckInTimeBetween(Long teacherId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    long countByUserIdAndStatus(Long userId, AttendanceStatus attendanceStatus);



    Optional<TeacherAttendance> findFirstByUserAndCheckInTimeBetweenOrderByCheckInTimeDesc(User teacher, LocalDateTime atStartOfDay, LocalDateTime atTime);

    Optional<TeacherAttendance> findFirstByUserOrderByCheckInTimeDesc(User teacher);
}
