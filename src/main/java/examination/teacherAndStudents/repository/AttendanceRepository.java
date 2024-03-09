package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.Attendance;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.utils.AttendanceStatus;
import examination.teacherAndStudents.utils.StudentTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Attendance findByUserAndDate(User student, LocalDate date);

    List<Attendance> findByUserAndDateBetween(User student, LocalDate startDate, LocalDate endDate);

    long countByUserId(Long userId);

    long countByUserIdAndStatus(Long userId, AttendanceStatus status);

    List<Attendance> findByUserInAndDateBetween(List<User> studentsInClass, LocalDate startDate, LocalDate endDate);

    long countByUserIdAndAndTerm(Long userId, StudentTerm studentTerm);

    long countByUserIdAndAndTermAndAndStatus(Long userId, StudentTerm studentTerm, AttendanceStatus present);
}
