package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.ClassCategoryDto;
import examination.teacherAndStudents.dto.ClassCategoryRequest;
import examination.teacherAndStudents.dto.ClassCategoryResponse;
import examination.teacherAndStudents.dto.SubClassDto;
import examination.teacherAndStudents.entity.ClassCategory;
import examination.teacherAndStudents.entity.SubClass;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClassCategoryService {

    Page<ClassCategoryResponse> getAllClassCategories(int pageNo, int pageSize, String sortBy);
    ClassCategoryResponse getClassCategoryById(Long id);
    ClassCategoryResponse createClassCategory(ClassCategoryRequest classCategoryRequest);
    ClassCategoryResponse updateClassCategory(Long id, ClassCategoryRequest classCategoryRequest);
    void deleteClassCategory(Long id);

}
