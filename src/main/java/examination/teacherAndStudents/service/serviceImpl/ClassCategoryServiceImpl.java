package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.dto.*;
import examination.teacherAndStudents.entity.AcademicYear;
import examination.teacherAndStudents.entity.ClassCategory;
import examination.teacherAndStudents.entity.SubClass;
import examination.teacherAndStudents.repository.AcademicYearRepository;
import examination.teacherAndStudents.repository.ClassCategoryRepository;
import examination.teacherAndStudents.repository.SubClassRepository;
import examination.teacherAndStudents.service.ClassCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassCategoryServiceImpl implements ClassCategoryService {
    private final ClassCategoryRepository classCategoryRepository;
    private final AcademicYearRepository academicYearRepository;

    @Override
        public Page<ClassCategoryResponse> getAllClassCategories(int pageNo, int pageSize, String sortBy) {

        try {
            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
            Page<ClassCategory> classCategories = classCategoryRepository.findAll(paging);
            return classCategories.map(this::mapToClassCategoryResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve all Class Categories: " + e.getMessage());
        }
    }

    @Override
    public ClassCategoryResponse getClassCategoryById(Long id) {
        try {
            Optional<ClassCategory> optionalClassCategory = classCategoryRepository.findById(id);
            if (optionalClassCategory.isPresent()) {
                return mapToClassCategoryResponse(optionalClassCategory.get());
            } else {
                throw new RuntimeException("Class Category not found with id: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve Class Category by id: " + e.getMessage());
        }
    }

    @Override
    public ClassCategoryResponse createClassCategory(ClassCategoryRequest classCategoryRequest) {
        try {
            Optional<AcademicYear> academicYear = academicYearRepository.findById(classCategoryRequest.getAcademicYearId());
            ClassCategory newClassCategory = new ClassCategory();
            newClassCategory.setCategoryName(classCategoryRequest.getCategoryName());
            newClassCategory.setAcademicYear(academicYear.get());
            ClassCategory savedClassCategory = classCategoryRepository.save(newClassCategory);
            return mapToClassCategoryResponse(savedClassCategory);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Class Category: " + e.getMessage());
        }
    }

    @Override
    public ClassCategoryResponse updateClassCategory(Long id, ClassCategoryRequest classCategoryRequest) {
        try {
            Optional<ClassCategory> optionalExistingClassCategory = classCategoryRepository.findById(id);
            Optional<AcademicYear> academicYear = academicYearRepository.findById(classCategoryRequest.getAcademicYearId());
            if (optionalExistingClassCategory.isPresent()) {
                ClassCategory existingClassCategory = optionalExistingClassCategory.get();
                existingClassCategory.setCategoryName(classCategoryRequest.getCategoryName());
                existingClassCategory.setAcademicYear(academicYear.get());
                ClassCategory updatedClassCategory = classCategoryRepository.save(existingClassCategory);
                return mapToClassCategoryResponse(updatedClassCategory);
            } else {
                throw new RuntimeException("Class Category not found with id: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update Class Category: " + e.getMessage());
        }
    }

    @Override
    public void deleteClassCategory(Long id) {
        try {
            classCategoryRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Class Category with id " + id + ": " + e.getMessage());
        }
    }

    private ClassCategoryResponse mapToClassCategoryResponse(ClassCategory classCategory) {
        ClassCategoryResponse classCategoryResponse = new ClassCategoryResponse();
        classCategoryResponse.setId(classCategory.getId());
        classCategoryResponse.setCategoryName(classCategory.getCategoryName());
        return classCategoryResponse;
    }


}
