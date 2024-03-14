package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.Position;
import examination.teacherAndStudents.entity.SubClass;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.utils.StudentTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Year;
import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Position findByUser(User user);

    List<Position> findAllBySubClassAndAndAverageScoreIsGreaterThanEqual(SubClass subClass, double averageScore);



    List<Position> findAllBySubClassIdAndYearAndTerm(Long classLevelId, Year year, StudentTerm term);

    Position findByUserAndSubClassAndYearAndTerm(User user, SubClass userClass, Year year, StudentTerm term);
}
