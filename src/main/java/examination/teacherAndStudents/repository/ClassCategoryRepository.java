package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.ClassCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassCategoryRepository extends JpaRepository<ClassCategory, Long> {
}
