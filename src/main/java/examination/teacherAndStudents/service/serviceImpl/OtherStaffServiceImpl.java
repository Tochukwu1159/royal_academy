package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.dto.OtherStaffRequest;
import examination.teacherAndStudents.dto.OtherStaffResponse;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.objectMapper.OtherStaffMapper;
import examination.teacherAndStudents.repository.UserRepository;
import examination.teacherAndStudents.service.OtherStaffService;
import examination.teacherAndStudents.utils.AccountUtils;
import examination.teacherAndStudents.utils.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OtherStaffServiceImpl implements OtherStaffService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtherStaffMapper otherStaffMapper;

    // Create (Add) other staff
    public OtherStaffResponse createOtherStaff(OtherStaffRequest otherStaffRequest) {
        try {
            User otherStaff = otherStaffMapper.mapToOtherStaff(otherStaffRequest);
            otherStaff.setUniqueRegistrationNumber(AccountUtils.generateOtherStaffId());
            otherStaff.setRoles(Roles.OTHERSTAFF);
            User savedOtherStaff = userRepository.save(otherStaff);
            return otherStaffMapper.mapToOtherStaffResponse(savedOtherStaff);
        } catch (Exception e) {
            // Handle any exceptions
            throw new CustomInternalServerException("Error creating other staff: " + e.getMessage());
        }
    }

    // Update other staff
    public OtherStaffResponse updateOtherStaff(Long otherStaffId, OtherStaffRequest updatedOtherStaff) {
        try {
            User existingOtherStaff = userRepository.findById(otherStaffId)
                    .orElseThrow(() -> new CustomNotFoundException("Other staff not found with ID: " + otherStaffId));

            // Update fields with values from the updatedOtherStaff request
            existingOtherStaff.setFirstName(updatedOtherStaff.getFirstName());
            existingOtherStaff.setLastName(updatedOtherStaff.getLastName());
            existingOtherStaff.setJobDescription(updatedOtherStaff.getJobDescription());
            existingOtherStaff.setPhoneNumber(updatedOtherStaff.getPhoneNumber());
            existingOtherStaff.setAddress(updatedOtherStaff.getAddress());
            // Update other fields as needed

            User updatedOtherStaffEntity = userRepository.save(existingOtherStaff);
            return otherStaffMapper.mapToOtherStaffResponse(updatedOtherStaffEntity);
        } catch (Exception e) {
            // Handle any exceptions
            throw new CustomInternalServerException("Error updating other staff: " + e.getMessage());
        }
    }

    // Get all other staff
    public List<OtherStaffResponse> findAllOtherStaff() {
        List<User> otherStaffList = userRepository.findAll();
        return otherStaffList.stream()
                .map(otherStaffMapper::mapToOtherStaffResponse)
                .collect(Collectors.toList());
    }

    // Get other staff by ID
    public OtherStaffResponse findOtherStaffById(Long otherStaffId) {
        User otherStaff = userRepository.findById(otherStaffId)
                .orElseThrow(() -> new CustomNotFoundException("Other staff not found with ID: " + otherStaffId));
        return otherStaffMapper.mapToOtherStaffResponse(otherStaff);
    }

    // Delete other staff
    public void deleteOtherStaff(Long otherStaffId) {
        try {
            userRepository.deleteById(otherStaffId);
        } catch (Exception e) {
            // Handle any exceptions
            throw new CustomInternalServerException("Error deleting other staff: " + e.getMessage());
        }
    }
}
