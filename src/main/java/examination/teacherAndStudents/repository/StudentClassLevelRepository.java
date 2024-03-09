package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.StudentClassLevel;
import examination.teacherAndStudents.entity.SubClass;
import examination.teacherAndStudents.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentClassLevelRepository extends JpaRepository<StudentClassLevel, Long> {
    StudentClassLevel findByUserAndStudentClass(User student, SubClass studentClass);
    List<StudentClassLevel> findAllByUser(User student);
    List<StudentClassLevel> findByStudentClassAndActiveIsTrue(StudentClassLevel studentClass);

    StudentClassLevel findByUserAndActiveIsTrue(User user);


    Optional<StudentClassLevel> findByStudentClass(SubClass studentClass);
}
