package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {
}
