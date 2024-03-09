package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.Driver;
import examination.teacherAndStudents.entity.Dues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository  extends JpaRepository<Driver, Long> {
}
