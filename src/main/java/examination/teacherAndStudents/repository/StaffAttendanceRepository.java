package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.StaffAttendance;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.utils.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StaffAttendanceRepository extends JpaRepository<StaffAttendance, Long> {

    List<StaffAttendance> findByCheckInTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<StaffAttendance> findByUserIdAndCheckInTimeBetween(Long teacherId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    long countByUserIdAndStatus(Long userId, AttendanceStatus attendanceStatus);



    Optional<StaffAttendance> findFirstByUserAndCheckInTimeBetweenOrderByCheckInTimeDesc(User teacher, LocalDateTime atStartOfDay, LocalDateTime atTime);

    Optional<StaffAttendance> findFirstByUserOrderByCheckInTimeDesc(User teacher);
}
