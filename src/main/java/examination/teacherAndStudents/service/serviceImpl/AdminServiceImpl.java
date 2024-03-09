package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.Security.SecurityConfig;
import examination.teacherAndStudents.dto.SickLeaveDTO;
import examination.teacherAndStudents.entity.SickLeave;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.repository.SickLeaveRepository;
import examination.teacherAndStudents.repository.UserRepository;
import examination.teacherAndStudents.service.AdminService;
import examination.teacherAndStudents.utils.Roles;
import examination.teacherAndStudents.utils.SickLeaveStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final SickLeaveRepository sickLeaveRepository;

    private final UserRepository userRepository;
    @Override
    public List<User> getAllStudents() {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new CustomNotFoundException("Please login as an Admin"); // Return unauthorized response for non-admin users
            }

            List<User> studentsList = userRepository.findUserByRoles(Roles.STUDENT);
            return studentsList;
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while fetching students " +e.getMessage());
        }
    }

        @Override
        public List<User> getAllTeachers () {
            try {
                String email = SecurityConfig.getAuthenticatedUserEmail();
                User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
                if (admin == null) {
                    throw new CustomNotFoundException("Please login as an Admin"); // Return unauthorized response for non-admin users
                }

                List<User> teachersList = userRepository.findUserByRoles(Roles.TEACHER);
                return teachersList;
            } catch (Exception e) {
                throw new CustomInternalServerException("An error occurred while fetching teachers "+e.getMessage());
            }
        }

    public void approveSickLeave(Long sickLeaveId) {
        SickLeave sickLeave = sickLeaveRepository.findById(sickLeaveId)
                .orElseThrow(() -> new CustomNotFoundException("Sick leave not found with ID: " + sickLeaveId));

        if (sickLeave.getStatus() == SickLeaveStatus.PENDING) {
            sickLeave.setStatus(SickLeaveStatus.APPROVED);
            // Additional logic for notifying teacher or updating records
            sickLeaveRepository.save(sickLeave);
        } else {
            throw new CustomNotFoundException("Sick leave is not in a pending state.");
        }
    }

    public void rejectSickLeave(Long sickLeaveId) {
        SickLeave sickLeave = sickLeaveRepository.findById(sickLeaveId)
                .orElseThrow(() -> new CustomNotFoundException("Sick leave not found with ID: " + sickLeaveId));

        if ((sickLeave.getStatus()) == SickLeaveStatus.PENDING) {
            sickLeave.setStatus(SickLeaveStatus.REJECTED);
            // Additional logic for notifying teacher or updating records
            sickLeaveRepository.save(sickLeave);
        } else {
            throw new CustomNotFoundException("Sick leave is not in a pending state.");
        }
    }


    public List<SickLeaveDTO> getAllSickLeaves() {
        List<SickLeave> allSickLeaves = sickLeaveRepository.findAll();
        // Convert SickLeave entities to DTOs if needed
        return allSickLeaves.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private SickLeave getSickLeaveById(Long sickLeaveId) {
        return sickLeaveRepository.findById(sickLeaveId)
                .orElseThrow(() -> new RuntimeException("SickLeave not found with ID: " + sickLeaveId));
    }

    private SickLeaveDTO convertToDTO(SickLeave sickLeave) {
        // Convert SickLeave entity to DTO if needed
        SickLeaveDTO sickLeaveDTO = new SickLeaveDTO();
        sickLeaveDTO.setId(sickLeave.getId());
        sickLeaveDTO.setStartDate(sickLeave.getStartDate());
        sickLeaveDTO.setEndDate(sickLeave.getEndDate());
        sickLeaveDTO.setReason(sickLeave.getReason());
        sickLeaveDTO.setStatus(sickLeave.getStatus());
        // Set other DTO fields as needed
        return sickLeaveDTO;
    }
    }