package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.StudyMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StudyMaterialRepository extends JpaRepository<StudyMaterial, Long> {
    List<StudyMaterial> findByCourseId(Long courseId);
    List<StudyMaterial> findByTeacherId(Long teacherId);
}