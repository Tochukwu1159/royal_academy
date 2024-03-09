package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.LessonNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonNoteRepository extends JpaRepository<LessonNote, Long> {
}
