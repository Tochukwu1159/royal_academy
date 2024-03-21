package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.dto.FormTeacherResponse;
import examination.teacherAndStudents.dto.PromoteStudentRequest;
import examination.teacherAndStudents.dto.SubClassRequest;
import examination.teacherAndStudents.dto.SubClassResponse;
import examination.teacherAndStudents.entity.*;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.NotFoundException;
import examination.teacherAndStudents.repository.*;
import examination.teacherAndStudents.service.SubClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubClassServiceImpl implements SubClassService {

    private final SubClassRepository subClassRepository;
    private final ClassCategoryRepository classCategoryRepository;
    private final UserRepository userRepository;
    private final FormTeacherRepository formTeacherRepository;
    private final PositionRepository positionRepository;

    @Override
    public List<SubClassResponse> getAllSubClasses() {
        try {
            List<SubClass> subClasses = subClassRepository.findAll();
            return subClasses.stream()
                    .map(this::mapToSubClassResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve all SubClasses: " + e.getMessage());
        }
    }

    @Override
    public SubClassResponse getSubClassById(Long id) {
        try {
            Optional<SubClass> optionalSubClass = subClassRepository.findById(id);
            if (optionalSubClass.isPresent()) {
                return mapToSubClassResponse(optionalSubClass.get());
            } else {
                throw new RuntimeException("SubClass not found with id: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve SubClass by id: " + e.getMessage());
        }
    }

    @Override
    public SubClassResponse createSubClass(SubClassRequest subClassRequest) {
        try {
            Optional<ClassCategory> existingCategory = classCategoryRepository.findById(subClassRequest.getCategoryId());
            if (existingCategory.isPresent()) {
                SubClass newSubClass = new SubClass();
                newSubClass.setSubClassName(subClassRequest.getSubClassName());
                newSubClass.setNumberOfStudents(subClassRequest.getNumberOfStudents());
                newSubClass.setClassUniqueUrl(subClassRequest.getClassUniqueUrl());
                newSubClass.setClassCategory(existingCategory.get());
                SubClass savedSubClass = subClassRepository.save(newSubClass);
                return mapToSubClassResponse(savedSubClass);
            } else {
                throw new RuntimeException("Class Category not found with id: " + subClassRequest.getCategoryId());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SubClass: " + e.getMessage());
        }
    }

    @Override
    public SubClassResponse updateSubClass(Long id, SubClassRequest subClassRequest) {
        try {
            Optional<SubClass> optionalExistingSubClass = subClassRepository.findById(id);
            if (optionalExistingSubClass.isPresent()) {
                SubClass existingSubClass = optionalExistingSubClass.get();
                existingSubClass.setSubClassName(subClassRequest.getSubClassName());
                existingSubClass.setNumberOfStudents(subClassRequest.getNumberOfStudents());
                existingSubClass.setClassUniqueUrl(subClassRequest.getClassUniqueUrl());
                SubClass updatedSubClass = subClassRepository.save(existingSubClass);
                return mapToSubClassResponse(updatedSubClass);
            } else {
                throw new RuntimeException("SubClass not found with id: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update SubClass: " + e.getMessage());
        }
    }

    @Override
    public void deleteSubClass(Long id) {
        try {
            subClassRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete SubClass with id " + id + ": " + e.getMessage());
        }
    }



    public FormTeacherResponse assignFormTeacher(Long teacherId, Long classCategoryId, Long subClassId) {
        try {
            ClassCategory classCategory = classCategoryRepository.findById(classCategoryId)
                    .orElseThrow(() -> new NotFoundException("Class category not found"));

            SubClass subClass = subClassRepository.findById(subClassId)
                    .orElseThrow(() -> new NotFoundException("Sub class not found"));

            User teacher = userRepository.findById(teacherId)
                    .orElseThrow(() -> new NotFoundException("Teacher not found"));

            FormTeacher formTeacher = new FormTeacher();
            formTeacher.setSubClass(subClass);

            formTeacher.setActive(true);
            FormTeacher savedFormTeacher = formTeacherRepository.save(formTeacher);

            FormTeacherResponse response = new FormTeacherResponse();
            response.setId(savedFormTeacher.getId());
            // Set other fields in the response as needed

            return response;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while assigning form teacher "+ e);
        }
    }

    @Override
    public PromoteStudentRequest promoteStudents(PromoteStudentRequest request) {
        ClassCategory currentClassCategory = classCategoryRepository.findById(request.getCurrentClassCategoryId())
                .orElseThrow(() -> new NotFoundException("Class category not found"));

        SubClass currentSubClass = subClassRepository.findById(request.getCurrentSubClassId())
                .orElseThrow(() -> new NotFoundException("Subclass not found"));

        ClassCategory PromotedClassCategory = classCategoryRepository.findById(request.getCurrentClassCategoryId())
                .orElseThrow(() -> new NotFoundException("Class category not found"));

        SubClass promotedSubClass = subClassRepository.findById(request.getCurrentSubClassId())
                .orElseThrow(() -> new NotFoundException("Subclass not found"));

        List<Position> positions = positionRepository.findAllBySubClassAndAndAverageScoreIsGreaterThanEqual(
                currentSubClass, request.getAverage());

        // Update students from fetched positions
        List<User> promotedStudents = new ArrayList<>();
        for (Position position : positions) {
            User student = position.getUser();
//           student.setClass(PromotedClassCategory);
//        student.setSubClass(promotedSubClass);
//        student.setCurrentAcademicYear(request.getPromotedAcademicYear());
            promotedStudents.add(student);
        }

        // Save updated students
        userRepository.saveAll(promotedStudents);

        return request;
    }

    private SubClassResponse mapToSubClassResponse(SubClass subClass) {
        SubClassResponse subClassResponse = new SubClassResponse();
        subClassResponse.setId(subClass.getId());
        subClassResponse.setSubClassName(subClass.getSubClassName());
        subClassResponse.setNumberOfStudents(subClass.getNumberOfStudents());
        subClassResponse.setClassUniqueUrl(subClass.getClassUniqueUrl());
        return subClassResponse;
    }
}
