package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.BusRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRouteRepository extends JpaRepository<BusRoute, Long> {
}