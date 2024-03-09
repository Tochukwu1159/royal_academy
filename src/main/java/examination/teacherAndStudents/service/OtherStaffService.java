package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.OtherStaffRequest;
import examination.teacherAndStudents.dto.OtherStaffResponse;

import java.util.List;

public interface OtherStaffService {
    OtherStaffResponse createOtherStaff(OtherStaffRequest otherStaffRequest);
    OtherStaffResponse updateOtherStaff(Long otherStaffId, OtherStaffRequest updatedOtherStaff);
    List<OtherStaffResponse> findAllOtherStaff();
    OtherStaffResponse findOtherStaffById(Long otherStaffId);
    void deleteOtherStaff(Long otherStaffId);
}
