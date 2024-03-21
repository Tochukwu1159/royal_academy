package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.*;
import examination.teacherAndStudents.entity.ClassCategory;
import examination.teacherAndStudents.entity.SubClass;
import examination.teacherAndStudents.service.ClassCategoryService;
import examination.teacherAndStudents.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/class-categories")
@RequiredArgsConstructor
public class ClassCategoryController {

    private final ClassCategoryService classCategoryService;

    @GetMapping("/all")
        public ResponseEntity<Page<ClassCategoryResponse>> getAllRoutes(@RequestParam(defaultValue = AccountUtils.PAGENO) Integer pageNo,
                @RequestParam(defaultValue = AccountUtils.PAGESIZE) Integer pageSize,
                @RequestParam(defaultValue = "id") String sortBy) {
        Page<ClassCategoryResponse> classCategories = classCategoryService.getAllClassCategories(pageNo, pageSize, sortBy);
        return ResponseEntity.ok(classCategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassCategoryResponse> getClassCategoryById(@PathVariable Long id) {
        ClassCategoryResponse classCategory = classCategoryService.getClassCategoryById(id);
        if (classCategory != null) {
            return ResponseEntity.ok(classCategory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ClassCategoryResponse> createClassCategory(@RequestBody ClassCategoryRequest classCategoryRequest) {
        try {
            ClassCategoryResponse newClassCategory = classCategoryService.createClassCategory(classCategoryRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(newClassCategory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassCategoryResponse> updateClassCategory(@PathVariable Long id, @RequestBody ClassCategoryRequest classCategoryRequest) {
        try {
            ClassCategoryResponse updatedClassCategory = classCategoryService.updateClassCategory(id, classCategoryRequest);
            if (updatedClassCategory != null) {
                return ResponseEntity.ok(updatedClassCategory);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassCategory(@PathVariable Long id) {
        try {
            classCategoryService.deleteClassCategory(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
