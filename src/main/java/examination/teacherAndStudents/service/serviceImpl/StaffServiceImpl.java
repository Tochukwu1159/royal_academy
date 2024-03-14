package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.dto.StaffRequest;
import examination.teacherAndStudents.dto.StaffResponse;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.objectMapper.StaffMapper;
import examination.teacherAndStudents.repository.UserRepository;
import examination.teacherAndStudents.service.StaffService;
import examination.teacherAndStudents.utils.AccountUtils;
import examination.teacherAndStudents.utils.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StaffMapper staffMapper;

    // Create (Add)  staff
    public StaffResponse createStaff(StaffRequest staffRequest) {
        try {
            User Staff = staffMapper.mapToStaff(staffRequest);
            Staff.setUniqueRegistrationNumber(AccountUtils.generateStaffId());
            Staff.setRoles(staffRequest.getRoles());
            User savedStaff = userRepository.save(Staff);
            return staffMapper.mapToStaffResponse(savedStaff);
        } catch (Exception e) {
            // Handle any exceptions
            throw new CustomInternalServerException("Error creating  staff: " + e.getMessage());
        }
    }

    // Update  staff
    public StaffResponse updateStaff(Long StaffId, StaffRequest updatedStaff) {
        try {
            User existingStaff = userRepository.findById(StaffId)
                    .orElseThrow(() -> new CustomNotFoundException(" staff not found with ID: " + StaffId));

            // Update fields with values from the updatedStaff request
            existingStaff.setFirstName(updatedStaff.getFirstName());
            existingStaff.setLastName(updatedStaff.getLastName());
            existingStaff.setPhoneNumber(updatedStaff.getPhoneNumber());
            existingStaff.setAddress(updatedStaff.getAddress());
            existingStaff.setBankName(updatedStaff.getBankName());
            existingStaff.setBankAccountName(updatedStaff.getBankAccountName());
            existingStaff.setAcademicQualification(updatedStaff.getAcademicQualification());
            existingStaff.setReligion(updatedStaff.getReligion());
            existingStaff.setBankAccountNumber(updatedStaff.getBankAccountNumber());
            existingStaff.setMiddleName(updatedStaff.getMiddleName());
            existingStaff.setMaritalStatus(updatedStaff.getMaritalStatus());
            existingStaff.setContractType(updatedStaff.getContractType());
            existingStaff.setSalary(updatedStaff.getSalary());
            // Update  fields as needed

            User updatedStaffEntity = userRepository.save(existingStaff);
            return staffMapper.mapToStaffResponse(updatedStaffEntity);
        } catch (Exception e) {
            // Handle any exceptions
            throw new CustomInternalServerException("Error updating  staff: " + e.getMessage());
        }
    }

    // Get all  staff
    public List<StaffResponse> findAllStaff() {
        List<User> staffList = userRepository.findAll();
        return staffList.stream()
                .map(staffMapper::mapToStaffResponse)
                .collect(Collectors.toList());
    }

    // Get  staff by ID
    public StaffResponse findStaffById(Long StaffId) {
        User staff = userRepository.findById(StaffId)
                .orElseThrow(() -> new CustomNotFoundException(" staff not found with ID: " + StaffId));
        return staffMapper.mapToStaffResponse(staff);
    }

    // Delete  staff
    public void deleteStaff(Long StaffId) {
        try {
            userRepository.deleteById(StaffId);
        } catch (Exception e) {
            // Handle any exceptions
            throw new CustomInternalServerException("Error deleting  staff: " + e.getMessage());
        }
    }



}
