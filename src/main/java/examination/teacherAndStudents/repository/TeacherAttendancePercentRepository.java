package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.TeacherAttendance;
import examination.teacherAndStudents.entity.TeacherAttendancePercent;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.utils.StudentTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherAttendancePercentRepository extends JpaRepository<TeacherAttendancePercent, Long> {
//    Optional<TeacherAttendancePercent> findByUserAndStudentTerm(User teacher, StudentTerm term);
}