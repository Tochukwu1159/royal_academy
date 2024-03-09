package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.ClassCategoryDto;
import examination.teacherAndStudents.dto.SubClassDto;
import examination.teacherAndStudents.entity.ClassCategory;
import examination.teacherAndStudents.entity.SubClass;
import examination.teacherAndStudents.service.ClassCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/class-categories")
public class ClassCategoryController {

    @Autowired
    private ClassCategoryService classCategoryService;

    @GetMapping
    public ResponseEntity<List<ClassCategory>> getAllClassCategories() {
        List<ClassCategory> classCategories = classCategoryService.getAllClassCategories();
        return ResponseEntity.ok(classCategories);
    }

    @PostMapping
    public ResponseEntity<ClassCategory> createClassCategory(@RequestBody ClassCategoryDto classCategory) {
        ClassCategory createdClassCategory = classCategoryService.createClassCategory(classCategory);
        return ResponseEntity.ok(createdClassCategory);
    }

    @PostMapping("/{classCategoryId}/add-subclass")
    public ResponseEntity<SubClass> addSubClass(
            @PathVariable Long classCategoryId,
            @RequestBody SubClassDto subClass) {
        SubClass addedSubClass = classCategoryService.addSubClass(classCategoryId, subClass);
        return ResponseEntity.ok(addedSubClass);
    }
}
