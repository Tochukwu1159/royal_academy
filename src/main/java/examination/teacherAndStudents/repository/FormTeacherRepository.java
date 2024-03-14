package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.FeeGroup;
import examination.teacherAndStudents.entity.FormTeacher;
import examination.teacherAndStudents.entity.SubClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Year;

public interface FormTeacherRepository extends JpaRepository<FormTeacher, Long> {
    FormTeacher findBySubClassAndYear(SubClass subClass, Year year);

}
