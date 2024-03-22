package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.Security.SecurityConfig;
import examination.teacherAndStudents.dto.DuesRequest;
import examination.teacherAndStudents.dto.DuesResponse;
import examination.teacherAndStudents.dto.DuesUpdateRequest;
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
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DuesServiceImpl implements DuesService {

    private final DuesRepository duesRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public DuesServiceImpl(DuesRepository duesRepository,
                           UserRepository userRepository,
                           ModelMapper modelMapper) {
        this.duesRepository = duesRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    public Page<DuesResponse> getAllDues(int pageNo, int pageSize, String sortBy) {
        String email = SecurityConfig.getAuthenticatedUserEmail();
        User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
        if (admin == null) {
            throw new AuthenticationFailedException("Please login as an Admin");
        }

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());

        Page<Dues> dues = duesRepository.findAll(paging);
        return dues.map((element) -> modelMapper.map(element, DuesResponse.class));
    }

    public DuesResponse getDuesById(Long id) {
        String email = SecurityConfig.getAuthenticatedUserEmail();
        User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
        if (admin == null) {
            throw new AuthenticationFailedException("Please login as an Admin");
        }
        Dues dues = duesRepository.findById(id).orElse(null);
        return modelMapper.map(dues, DuesResponse.class);
    }

    public DuesResponse createDuesForASudent(Long id, DuesRequest duesRequest) {
        String email = SecurityConfig.getAuthenticatedUserEmail();
        User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
        if (admin == null) {
            throw new AuthenticationFailedException("Please login as an Admin");
        }
        // Validate duesRequest fields
        validateDuesRequest(duesRequest);

        Optional<User> studentOptional = userRepository.findById(id);
        User student = studentOptional.orElseThrow(() -> new CustomNotFoundException("Student not found with id: " + duesRequest.getStudentId()));

        Dues studentDues = new Dues();
        studentDues.setUser(student);
        studentDues.setTimeAdd(LocalDateTime.now());
        studentDues.setYear(Year.now());
        studentDues.setPurpose(duesRequest.getPurpose());
        studentDues.setAmount(duesRequest.getAmount());
        studentDues.setStatus(DuesStatus.PENDING);
        Dues studentDue = duesRepository.save(studentDues);
        return modelMapper.map(studentDue, DuesResponse.class);
    }
    public List<DuesResponse> createDues(DuesRequest duesRequest) {
        String email = SecurityConfig.getAuthenticatedUserEmail();
        User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
        if (admin == null) {
            throw new AuthenticationFailedException("Please login as an Admin");
        }
        // Validate duesRequest fields
        validateDuesRequest(duesRequest);

        Optional<User> studentOptional = userRepository.findById(duesRequest.getStudentId());
        User student = studentOptional.orElseThrow(() -> new CustomNotFoundException("Student not found with id: " + duesRequest.getStudentId()));

        List<User> users = userRepository.findAll();
        List<Dues> duesList = new ArrayList<>();

        for (User user : users) {
            Dues dues = new Dues();
            dues.setUser(user);
            dues.setTimeAdd(LocalDateTime.now());
            dues.setYear(Year.now());
            dues.setPurpose(duesRequest.getPurpose());
            dues.setAmount(duesRequest.getAmount());
            dues.setStatus(DuesStatus.PENDING);
            duesList.add(dues);
        }

        return duesRepository.saveAll(duesList)
                .stream().map((element) -> modelMapper.map(element, DuesResponse.class))
                .collect(Collectors.toList());
    }

    private void validateDuesRequest(DuesRequest duesRequest) {
        if (duesRequest.getAmount() == null || duesRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (duesRequest.getPurpose() != null && duesRequest.getPurpose().isEmpty()) {
            throw new IllegalArgumentException("Purpose cannot be empty");
        }
    }

    public List<DuesResponse> updateDuesForAllUsers(Long dueId, DuesUpdateRequest duesUpdateRequest) {
        String email = SecurityConfig.getAuthenticatedUserEmail();
        User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
        if (admin == null) {
            throw new AuthenticationFailedException("Please login as an Admin");
        }

        // Validate duesUpdateRequest fields
        validateDuesUpdateRequest(duesUpdateRequest);

        // Retrieve the due by its ID
        Optional<Dues> optionalDue = duesRepository.findById(dueId);
        Dues due = optionalDue.orElseThrow(() -> new CustomNotFoundException("Dues not found with ID: " + dueId));

        // Update the due information
        if (duesUpdateRequest.getPurpose() != null) {
            due.setPurpose(duesUpdateRequest.getPurpose());
        }
        if (duesUpdateRequest.getAmount() != null) {
            due.setAmount(duesUpdateRequest.getAmount());
        }
        if (duesUpdateRequest.getStatus() != null) {
            due.setStatus(duesUpdateRequest.getStatus());
        }

        // Save the updated due for all users
        List<User> users = userRepository.findAll();
        List<Dues> updatedDuesList = new ArrayList<>();
        for (User user : users) {
            Dues updatedDue = new Dues();
            updatedDue.setId(due.getId());
            updatedDue.setUser(user);
            updatedDue.setTimeAdd(LocalDateTime.now());
            updatedDue.setYear(due.getYear());
            updatedDue.setPurpose(due.getPurpose());
            updatedDue.setAmount(due.getAmount());
            updatedDue.setStatus(due.getStatus());
            updatedDuesList.add(updatedDue);
        }

        return duesRepository.saveAll(updatedDuesList)
                .stream().map((element) -> modelMapper.map(element, DuesResponse.class))
                .collect(Collectors.toList());
    }
    public void validateDuesUpdateRequest(DuesUpdateRequest duesUpdateRequest) {
        if (duesUpdateRequest.getPurpose() != null && duesUpdateRequest.getPurpose().isEmpty()) {
            throw new IllegalArgumentException("Purpose cannot be empty");
        }
        if (duesUpdateRequest.getAmount() != null && duesUpdateRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount should be greater than zero");
        }
    }


    public DuesResponse updateDuesForAStudent(Long id, DuesRequest updatedDues) {
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
        existingDues.setTimeAdd(LocalDateTime.now());
        existingDues.setStatus(DuesStatus.PENDING);
        Dues updatedDue = duesRepository.save(existingDues);
        return modelMapper.map(updatedDue, DuesResponse.class);
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

