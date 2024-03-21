package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.ClassCategory;
import examination.teacherAndStudents.entity.SubClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubClassRepository extends JpaRepository<SubClass, Long> {
   SubClass findByClassCategory(ClassCategory classCategory);

   SubClass findByIdAndClassCategory(long subclassId, ClassCategory classCategory);


;}

