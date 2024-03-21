package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.SubClassRequest;
import examination.teacherAndStudents.dto.SubClassResponse;
import examination.teacherAndStudents.service.SubClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subclasses")
@RequiredArgsConstructor
public class SubClassController {

    private final SubClassService subClassService;

    @GetMapping
    public ResponseEntity<List<SubClassResponse>> getAllSubClasses() {
        List<SubClassResponse> subClasses = subClassService.getAllSubClasses();
        return new ResponseEntity<>(subClasses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubClassResponse> getSubClassById(@PathVariable Long id) {
        SubClassResponse subClass = subClassService.getSubClassById(id);
        return subClass != null ?
                new ResponseEntity<>(subClass, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<SubClassResponse> createSubClass(@RequestBody SubClassRequest subClassRequest) {
        SubClassResponse createdSubClass = subClassService.createSubClass(subClassRequest);
        return new ResponseEntity<>(createdSubClass, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubClassResponse> updateSubClass(@PathVariable Long id, @RequestBody SubClassRequest subClassRequest) {
        SubClassResponse updatedSubClass = subClassService.updateSubClass(id, subClassRequest);
        return updatedSubClass != null ?
                new ResponseEntity<>(updatedSubClass, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubClass(@PathVariable Long id) {
        subClassService.deleteSubClass(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
