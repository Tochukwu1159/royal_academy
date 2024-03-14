package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.ClassCategoryResponse;
import examination.teacherAndStudents.dto.DormitoryRequest;
import examination.teacherAndStudents.dto.DormitoryRoomRequest;
import examination.teacherAndStudents.dto.DormitoryRoomResponse;
import examination.teacherAndStudents.entity.Dormitory;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface DormitoryRoomService {
    Page<DormitoryRoomResponse> getAllDormitoryRooms(int pageNo, int pageSize, String sortBy);
    Optional<DormitoryRoomResponse> getDormitoryRoomById(Long hostelId);
    DormitoryRoomResponse createDormitoryRoom(DormitoryRoomRequest hostel);
    DormitoryRoomResponse updateDormitoryRoom(Long hostelId, DormitoryRoomRequest updatedDormitory);
    void deleteDormitoryRoom(Long hostelId);
    List<DormitoryRoomResponse> getAllAvailableDormitoryRooms();

    String  addStudentToDormitory(Long studentId, Long hostelId);
    boolean payForDormitory(Long hostelId);
}
