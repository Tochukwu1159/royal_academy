package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.*;
import examination.teacherAndStudents.utils.StudentTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Year;
import java.util.List;
import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findAllByUserAndSubClassAndYearAndTerm(User user, SubClass userClass, Year year, StudentTerm term);

    Result findByUserAndSubClassIdAndSubjectNameAndYearAndTerm(User student, Long subClassLevelId, String subjectName, Year year, StudentTerm term);

    List<Result> findAllByUserIdAndSubClassAndYearAndTerm(Long userId, SubClass studentClass, Year year, StudentTerm term);
}