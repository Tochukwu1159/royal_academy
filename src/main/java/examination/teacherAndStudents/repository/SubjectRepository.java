package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.Score;
import examination.teacherAndStudents.entity.SubClass;
import examination.teacherAndStudents.entity.Subject;
import examination.teacherAndStudents.utils.StudentTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Year;
import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findByName(String subject);

    Optional<Subject> findByIdAndTerm(Long subjectId, StudentTerm term);

    List<Subject> findAllBySubClass(SubClass subClass);


}
