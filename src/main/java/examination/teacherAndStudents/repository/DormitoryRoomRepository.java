package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.Dormitory;
import examination.teacherAndStudents.entity.DormitoryRooms;
import examination.teacherAndStudents.utils.AvailabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface DormitoryRoomRepository extends JpaRepository<DormitoryRooms, Long> {
    List<DormitoryRooms> findByAvailabilityStatus(AvailabilityStatus available);

    DormitoryRooms findByDormitory(Dormitory dormitory);
}