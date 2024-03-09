package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
