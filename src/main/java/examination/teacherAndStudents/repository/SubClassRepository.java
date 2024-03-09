package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.SubClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubClassRepository extends JpaRepository<SubClass, Long> {

    SubClass findBySubClassName(SubClass subClassName)
;}

