package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.StaffAttendance;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.utils.AttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StaffAttendanceRepository extends JpaRepository<StaffAttendance, Long> {

    List<StaffAttendance> findAllByStaffUniqueRegNumberAndAndCheckInTimeBetween(String staffUniqueNumber, LocalDateTime startDate, LocalDateTime endDate);

    Page<StaffAttendance> findAllByCheckInTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    long countByUserIdAndStatus(Long userId, AttendanceStatus attendanceStatus);



    Optional<StaffAttendance> findFirstByUserAndCheckInTimeBetweenOrderByCheckInTimeDesc(User teacher, LocalDateTime atStartOfDay, LocalDateTime atTime);

    Optional<StaffAttendance> findFirstByUserOrderByCheckInTimeDesc(User teacher);
}
