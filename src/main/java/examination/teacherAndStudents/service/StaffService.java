package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.StaffRequest;
import examination.teacherAndStudents.dto.StaffResponse;

import java.util.List;

public interface StaffService {
    StaffResponse createStaff(StaffRequest staffRequest);
    StaffResponse updateStaff(Long staffId, StaffRequest updatedStaff);
    List<StaffResponse> findAllStaff();
    StaffResponse findStaffById(Long StaffId);
    void deleteStaff(Long StaffId);
}
