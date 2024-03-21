package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Year;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {

    AcademicYear findByYear(Year year);
}
