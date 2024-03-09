package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.SchoolEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SchoolEventRepository extends JpaRepository<SchoolEvent, Long> {

    List<SchoolEvent> findByEventDateBetween(LocalDate startDate, LocalDate endDate);
}
