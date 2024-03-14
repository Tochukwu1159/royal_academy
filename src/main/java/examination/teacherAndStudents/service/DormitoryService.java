package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.DormitoryRequest;
import examination.teacherAndStudents.entity.Dormitory;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface DormitoryService {
    Page<Dormitory> getAllDormitorys(int pageNo, int pageSize, String sortBy);
    Optional<Dormitory> getDormitoryById(Long hostelId);
    Dormitory createDormitory(DormitoryRequest hostel);
    Dormitory updateDormitory(Long hostelId, DormitoryRequest updatedDormitory);
    void deleteDormitory(Long hostelId);

}
