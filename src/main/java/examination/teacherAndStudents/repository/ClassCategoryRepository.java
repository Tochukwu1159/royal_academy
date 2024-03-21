package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.AcademicYear;
import examination.teacherAndStudents.entity.ClassCategory;
import examination.teacherAndStudents.entity.SubClass;
import examination.teacherAndStudents.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassCategoryRepository extends JpaRepository<ClassCategory, Long> {
    ClassCategory findByUserAndStudentClass(User student, SubClass studentClass);

    ClassCategory findByIdAndAcademicYear(Long classCategoryId, AcademicYear academicYear);

    List<ClassCategory> findByStudentClassAndActiveIsTrue(ClassCategory studentClass);

    ClassCategory findByUserAndActiveIsTrue(User user);


    Optional<ClassCategory> findByStudentClass(SubClass studentClass);
}
