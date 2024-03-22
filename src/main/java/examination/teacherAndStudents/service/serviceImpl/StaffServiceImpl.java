package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.dto.StaffRequest;
import examination.teacherAndStudents.dto.StaffResponse;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.error_handler.EntityNotFoundException;
import examination.teacherAndStudents.objectMapper.StaffMapper;
import examination.teacherAndStudents.repository.UserRepository;
import examination.teacherAndStudents.service.StaffService;
import examination.teacherAndStudents.utils.AccountUtils;
import examination.teacherAndStudents.utils.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final UserRepository userRepository;

    private final StaffMapper staffMapper;

    // Create (Add)  staff
    public StaffResponse createStaff(StaffRequest staffRequest) {
        try {
            User staff = staffMapper.mapToStaff(staffRequest);
            staff.setUniqueRegistrationNumber(AccountUtils.generateStaffId());
            staff.setRoles(staffRequest.getRoles());
            User savedStaff = userRepository.save(staff);
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
    public Page<StaffResponse> findAllStaff(String filter, int page, int size, String sortBy) {
        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<User> staffPage = userRepository.findAllByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseOrId(filter, filter, Long.valueOf(filter), paging);
        return staffPage.map(staffMapper::mapToStaffResponse);
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

    public StaffResponse deactivateStaff(String uniqueRegistrationNumber) {
        try {
            Optional<User> optionalStaff = userRepository.findByUniqueRegistrationNumber(uniqueRegistrationNumber);
            if (optionalStaff.isPresent()) {
                User staff = optionalStaff.get();
                staff.setDeactivate(true);
                 userRepository.save(staff);
                 return staffMapper.mapToStaffResponse(staff);
            } else {
                throw new EntityNotFoundException("Staff not found with registration number: " + uniqueRegistrationNumber);
            }
        } catch (Exception e) {
            throw new CustomInternalServerException("Error deactivating student" +e);
        }
    }



}
