package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.Dues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DueRepository extends JpaRepository<Dues, Long> {
}
