package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.TeacherRequest;
import examination.teacherAndStudents.dto.TeacherResponse;
import examination.teacherAndStudents.service.TeacherService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping("/create")
    public ResponseEntity<TeacherResponse> createTeacher(@RequestBody TeacherRequest teacherRequest) throws MessagingException {
            TeacherResponse createdTeacher = teacherService.createTeacher(teacherRequest);
            return new ResponseEntity<>(createdTeacher, HttpStatus.CREATED);
    }

    @GetMapping("/{teacherId}")
    public ResponseEntity<TeacherResponse> getTeacherById(@PathVariable Long teacherId) {
        TeacherResponse teacher = teacherService.getTeacherById(teacherId);
        return new ResponseEntity<>(teacher, HttpStatus.OK);

    }

    @GetMapping("/all")
    public ResponseEntity<List<TeacherResponse>> getAllTeachers() {
        List<TeacherResponse> teachers = teacherService.findAllTeachers();
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }

    @PutMapping("/update/{teacherId}")
    public ResponseEntity<TeacherResponse> updateTeacher(
            @PathVariable Long teacherId,
            @RequestBody TeacherRequest updatedTeacher
    ) {
        TeacherResponse updatedTeacherResponse = teacherService.updateTeacher(teacherId, updatedTeacher);
        return new ResponseEntity<>(updatedTeacherResponse, HttpStatus.OK);

    }

    @DeleteMapping("/delete/{teacherId}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long teacherId) {
        teacherService.deleteTeacher(teacherId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
