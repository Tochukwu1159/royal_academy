package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.Security.SecurityConfig;
import examination.teacherAndStudents.dto.SubjectRequest;
import examination.teacherAndStudents.dto.SubjectResponse;
import examination.teacherAndStudents.entity.SubClass;
import examination.teacherAndStudents.entity.Subject;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.objectMapper.SubjectMapper;
import examination.teacherAndStudents.repository.SubClassRepository;
import examination.teacherAndStudents.repository.SubjectRepository;
import examination.teacherAndStudents.repository.UserRepository;
import examination.teacherAndStudents.service.SubjectService;
import examination.teacherAndStudents.utils.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final SubClassRepository subClassRepository;

    private final SubjectMapper subjectMapper;


    public SubjectResponse createSubject(SubjectRequest subjectRequest) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new CustomNotFoundException("Please login as an Admin"); // Return unauthorized response for non-admin users
            }

            SubClass subClass = subClassRepository.findById(subjectRequest.getSubClassId()).orElse(null);
            if (subClass == null) {
                throw new CustomNotFoundException("Class not found");
            }
            Subject subject = subjectMapper.mapToSubject(subjectRequest);
            subject.setSubClass(subClass);
            subjectRepository.save(subject);
           return subjectMapper.mapToSubjectResponse(subject);

        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while creating the subject " + e.getMessage());
        }
    }

    public Subject updateSubject(Long subjectId, SubjectRequest updatedSubjectRequest) {
        try {
            // Check if the subject exists
            Subject existingSubject = subjectRepository.findById(subjectId).orElse(null);
            if (existingSubject == null) {
                throw new CustomNotFoundException("Subject not found");
            }

            // Check if the authenticated user is an admin
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new CustomNotFoundException("Please login as an Admin");
            }

            // Update the subject details
            existingSubject.setName(updatedSubjectRequest.getName());

            // Save the updated subject
            return subjectRepository.save(existingSubject);
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while updating the subject " + e.getMessage());
        }
    }

    public Subject findSubjectById(Long subjectId) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new CustomNotFoundException("Please login as an Admin"); // Return unauthorized response for non-admin users
            }

            return subjectRepository.findById(subjectId).orElse(null);
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while finding the subject " +  e.getMessage());
        }
    }

    public List<Subject> findAllSubjects() {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new CustomNotFoundException("Please login as an Admin"); // Return unauthorized response for non-admin users
            }

            return subjectRepository.findAll();
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while fetching all subjects "+  e.getMessage());
        }
    }
}

