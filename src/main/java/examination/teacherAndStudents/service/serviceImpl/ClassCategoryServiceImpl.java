package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.dto.ClassCategoryDto;
import examination.teacherAndStudents.dto.SubClassDto;
import examination.teacherAndStudents.entity.ClassCategory;
import examination.teacherAndStudents.entity.SubClass;
import examination.teacherAndStudents.repository.ClassCategoryRepository;
import examination.teacherAndStudents.repository.SubClassRepository;
import examination.teacherAndStudents.service.ClassCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ClassCategoryServiceImpl implements ClassCategoryService {
    @Autowired
    private ClassCategoryRepository classCategoryRepository;

    @Autowired
    private SubClassRepository subClassRepository;

    public List<ClassCategory> getAllClassCategories() {
        return classCategoryRepository.findAll();
    }

    public ClassCategory createClassCategory(ClassCategoryDto classCategory) {
        ClassCategory studentClassLevel = new ClassCategory();
        studentClassLevel.setCategoryName(classCategory.getCategoryName());
        return classCategoryRepository.save(studentClassLevel);
    }

    public SubClass addSubClass(Long classCategoryId, SubClassDto subClass) {
        ClassCategory classCategory = classCategoryRepository.getById(classCategoryId);
        SubClass studentSubClass = new SubClass();
        studentSubClass.setSubClassName(subClass.getSubClassName());
//        studentSubClass.setYear(subClass.getYear());
        studentSubClass.setClassCategory(classCategory);
        classCategory.getSubClasses().add(studentSubClass);
        subClassRepository.save(studentSubClass);
        return studentSubClass;
    }
}
