package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.Security.SecurityConfig;
import examination.teacherAndStudents.dto.SickLeaveDTO;
import examination.teacherAndStudents.dto.UserSummaryResponse;
import examination.teacherAndStudents.entity.SickLeave;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.repository.SickLeaveRepository;
import examination.teacherAndStudents.repository.UserRepository;
import examination.teacherAndStudents.service.AdminService;
import examination.teacherAndStudents.utils.Roles;
import examination.teacherAndStudents.utils.SickLeaveStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final SickLeaveRepository sickLeaveRepository;
    private final UserRepository userRepository;

    public UserSummaryResponse getUserSummary() {
        try {
            long totalStudents = userRepository.countByRoles(Roles.STUDENT);
            long totalTeachers = userRepository.countByRoles(Roles.TEACHER);
            long totalDrivers = userRepository.countByRoles(Roles.DRIVER);
            long totalGateMen = userRepository.countByRoles(Roles.GATEMAN);
            long totalAdmins = userRepository.countByRoles(Roles.ADMIN);
            long totalLibrarians = userRepository.countByRoles(Roles.LIBRARIAN);

            long totalParents = userRepository.countByStudentGuardianNameIsNotNull();

            return UserSummaryResponse.builder()
                    .totalStudents(totalStudents)
                    .totalTeachers(totalTeachers)
                    .totalParents(totalParents)
                    .totalDrivers(totalDrivers)
                    .totalGatemen(totalGateMen)
                    .totalAdmins(totalAdmins)
                    .totalLibrarians(totalLibrarians)
                    .build();
        } catch (CustomNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomInternalServerException("Error fetching user summary: " + e.getMessage());
        }
    }

    @Override
    public Page<User> getAllStudents(int pageNo, int pageSize, String sortBy) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new CustomNotFoundException("Please login as an Admin"); // Return unauthorized response for non-admin users
            }

            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());

            Page<User> studentsPage = userRepository.findAllByRoles(Roles.STUDENT, paging);
            return studentsPage;
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while fetching students: " + e.getMessage());
        }
    }

    @Override
    public Page<User> getAllTeachers(int pageNo, int pageSize, String sortBy) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new CustomNotFoundException("Please login as an Admin");
            }
            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());

            Page<User> teachersList = userRepository.findAllByRoles(Roles.TEACHER, paging);
            return teachersList;
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while fetching teachers: " + e.getMessage());
        }
    }

    @Override
    public void approveSickLeave(Long sickLeaveId) {
        try {
            SickLeave sickLeave = sickLeaveRepository.findById(sickLeaveId)
                    .orElseThrow(() -> new CustomNotFoundException("Sick leave not found with ID: " + sickLeaveId));

            if (sickLeave.getStatus() == SickLeaveStatus.PENDING) {
                sickLeave.setStatus(SickLeaveStatus.APPROVED);
                sickLeaveRepository.save(sickLeave);
            } else {
                throw new CustomNotFoundException("Sick leave is not in a pending state.");
            }
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while approving sick leave: " + e.getMessage());
        }
    }

    @Override
    public void rejectSickLeave(Long sickLeaveId) {
        try {
            SickLeave sickLeave = sickLeaveRepository.findById(sickLeaveId)
                    .orElseThrow(() -> new CustomNotFoundException("Sick leave not found with ID: " + sickLeaveId));

            if ((sickLeave.getStatus()) == SickLeaveStatus.PENDING) {
                sickLeave.setStatus(SickLeaveStatus.REJECTED);
                sickLeaveRepository.save(sickLeave);
            } else {
                throw new CustomNotFoundException("Sick leave is not in a pending state.");
            }
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while rejecting sick leave: " + e.getMessage());
        }
    }

    @Override
    public Page<SickLeaveDTO> getAllSickLeaves(int pageNo, int pageSize, String sortBy) {
        try {
            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
            Page<SickLeave> allSickLeaves = sickLeaveRepository.findAll(paging);
            return allSickLeaves.map(this::convertToDTO);
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while fetching all sick leaves: " + e.getMessage());
        }
    }

    private SickLeave getSickLeaveById(Long sickLeaveId) {
        return sickLeaveRepository.findById(sickLeaveId)
                .orElseThrow(() -> new CustomNotFoundException("SickLeave not found with ID: " + sickLeaveId));
    }

    private SickLeaveDTO convertToDTO(SickLeave sickLeave) {
        SickLeaveDTO sickLeaveDTO = new SickLeaveDTO();
        sickLeaveDTO.setId(sickLeave.getId());
        sickLeaveDTO.setStartDate(sickLeave.getStartDate());
        sickLeaveDTO.setEndDate(sickLeave.getEndDate());
        sickLeaveDTO.setReason(sickLeave.getReason());
        sickLeaveDTO.setStatus(sickLeave.getStatus());
        return sickLeaveDTO;
    }
}
