package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.Score;
import examination.teacherAndStudents.entity.SubClass;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.utils.StudentTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Year;
import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {


    List<Score> findScoreByUser(User student);


    Score findByUserAndSubClassIdAndSubjectNameAndYearAndTerm(User student, long subClassId, String name, Year year, StudentTerm term);

    List<Score> findAllByUserAndSubClassAndYearAndTerm(User user, SubClass userClass, Year year, StudentTerm term);


    // Additional methods if needed
}
