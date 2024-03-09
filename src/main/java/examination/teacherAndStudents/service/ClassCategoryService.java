package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.ClassCategoryDto;
import examination.teacherAndStudents.dto.SubClassDto;
import examination.teacherAndStudents.entity.ClassCategory;
import examination.teacherAndStudents.entity.SubClass;

import java.util.List;

public interface ClassCategoryService {

    List<ClassCategory> getAllClassCategories();
    ClassCategory createClassCategory(ClassCategoryDto classCategory);
    SubClass addSubClass(Long classCategoryId, SubClassDto subClass);
}
