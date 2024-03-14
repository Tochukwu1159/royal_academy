package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.Dormitory;
import examination.teacherAndStudents.utils.AvailabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DormitoryRepository extends JpaRepository<Dormitory, Long> {

}
