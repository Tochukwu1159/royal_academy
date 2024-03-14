package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.FeeGroup;
import examination.teacherAndStudents.entity.LessonNote;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FeeGroupRepository extends JpaRepository<FeeGroup, Long> {

}
