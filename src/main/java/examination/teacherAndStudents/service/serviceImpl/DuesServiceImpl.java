package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.Security.SecurityConfig;
import examination.teacherAndStudents.dto.DuesRequest;
import examination.teacherAndStudents.entity.Dormitory;
import examination.teacherAndStudents.entity.Dues;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.error_handler.AuthenticationFailedException;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.repository.DuesRepository;
import examination.teacherAndStudents.repository.UserRepository;
import examination.teacherAndStudents.service.DuesService;

import examination.teacherAndStudents.utils.DuesStatus;
import examination.teacherAndStudents.utils.Roles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
public class DuesServiceImpl implements DuesService {

    private final DuesRepository duesRepository;
    private final UserRepository userRepository;

    public DuesServiceImpl(DuesRepository duesRepository,
                           UserRepository userRepository) {
        this.duesRepository = duesRepository;
        this.userRepository = userRepository;
    }


    public Page<Dues> getAllDues(int pageNo, int pageSize, String sortBy) {
        String email = SecurityConfig.getAuthenticatedUserEmail();
        User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
        if (admin == null) {
            throw new AuthenticationFailedException("Please login as an Admin");
        }

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());

        return duesRepository.findAll(paging);
    }

    public Dues getDuesById(Long id) {
        String email = SecurityConfig.getAuthenticatedUserEmail();
        User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
        if (admin == null) {
            throw new AuthenticationFailedException("Please login as an Admin");
        }
        return duesRepository.findById(id).orElse(null);
    }

    public Dues createDues(DuesRequest duesRequest) {
        String email = SecurityConfig.getAuthenticatedUserEmail();
        User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
        if (admin == null) {
            throw new AuthenticationFailedException("Please login as an Admin");
        }
        // Validate duesRequest fields
        validateDuesRequest(duesRequest);

        Optional<User> studentOptional = userRepository.findById(duesRequest.getStudentId());
        User student = studentOptional.orElseThrow(() -> new CustomNotFoundException("Student not found with id: " + duesRequest.getStudentId()));

        Dues studentDues = new Dues();
        studentDues.setUser(student);
        studentDues.setTerm(duesRequest.getTerm());
        studentDues.setYear(Year.now());
        studentDues.setPurpose(duesRequest.getPurpose());
        studentDues.setAmount(duesRequest.getAmount());
        studentDues.setStatus(DuesStatus.PENDING);
        return duesRepository.save(studentDues);
    }
    private void validateDuesRequest(DuesRequest duesRequest) {
        // Implement validation logic for duesRequest fields
        if (duesRequest.getAmount() == null || duesRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        // Add more validation as needed
    }

    public Dues updateDues(Long id, DuesRequest updatedDues) {
        String email = SecurityConfig.getAuthenticatedUserEmail();
        User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
        if (admin == null) {
            throw new AuthenticationFailedException("Please login as an Admin");
        }
        // Validate updatedDues fields
        validateDuesRequest(updatedDues);
        Dues existingDues = duesRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Dues not found with id: " + id));

        existingDues.setPurpose(updatedDues.getPurpose());
        existingDues.setAmount(updatedDues.getAmount());
        existingDues.setTerm(updatedDues.getTerm());
        existingDues.setStatus(DuesStatus.PENDING);
        return duesRepository.save(existingDues);
    }
    public boolean deleteDues(Long id) {
        String email = SecurityConfig.getAuthenticatedUserEmail();
        User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
        if (admin == null) {
            throw new AuthenticationFailedException("Please login as an Admin");
        }

        try {
            Dues existingDues = duesRepository.findById(id)
                    .orElseThrow(() -> new CustomNotFoundException("Dues not found with id: " + id));

            duesRepository.delete(existingDues);
            return true;
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while deleting the dues entry"+ e.getMessage());
        }
    }
}

