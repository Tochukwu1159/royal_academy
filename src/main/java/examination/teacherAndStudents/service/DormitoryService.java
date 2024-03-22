package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.DormitoryRequest;
import examination.teacherAndStudents.dto.DormitoryResponse;
import examination.teacherAndStudents.entity.Dormitory;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface DormitoryService {
    Page<DormitoryResponse> getAllDormitorys(int pageNo, int pageSize, String sortBy);
    Optional<DormitoryResponse> getDormitoryById(Long hostelId);
    DormitoryResponse createDormitory(DormitoryRequest hostel);
    DormitoryResponse updateDormitory(Long hostelId, DormitoryRequest updatedDormitory);
    void deleteDormitory(Long hostelId);

}
