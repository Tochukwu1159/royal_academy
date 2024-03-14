package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.DormitoryRequest;
import examination.teacherAndStudents.entity.Dormitory;
import examination.teacherAndStudents.service.DormitoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class Controller {
    @Autowired
    private DormitoryService dormitoryService;
    @PostMapping("/add")
    public ResponseEntity<Dormitory> createHostel(@RequestBody DormitoryRequest hostel) {
        Dormitory createdDormitory = dormitoryService.createDormitory(hostel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDormitory);
    }


}
