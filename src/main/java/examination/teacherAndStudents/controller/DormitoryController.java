package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.DormitoryRequest;
import examination.teacherAndStudents.dto.DormitoryRoomResponse;
import examination.teacherAndStudents.entity.Dormitory;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.error_handler.InsufficientBalanceException;
import examination.teacherAndStudents.error_handler.NotFoundException;
import examination.teacherAndStudents.service.DormitoryService;
import examination.teacherAndStudents.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/dormitorys")
public class DormitoryController {

    @Autowired
    private DormitoryService dormitoryService;

    @GetMapping
        public ResponseEntity<Page<Dormitory>> getAllDormitorys(@RequestParam(defaultValue = AccountUtils.PAGENO) Integer pageNo,
                @RequestParam(defaultValue = AccountUtils.PAGESIZE) Integer pageSize,
                @RequestParam(defaultValue = "id") String sortBy) {
        Page<Dormitory> dormitories = dormitoryService.getAllDormitorys(pageNo, pageSize, sortBy);
        return ResponseEntity.ok(dormitories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dormitory> getDormitoryById(@PathVariable Long id) {
        Optional<Dormitory> dormitory = dormitoryService.getDormitoryById(id);
        return ResponseEntity.ok(dormitory.get());
    }

    @PostMapping("/add")
    public ResponseEntity<Dormitory> createDormitory(@RequestBody DormitoryRequest dormitory) {
        Dormitory createdDormitory = dormitoryService.createDormitory(dormitory);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDormitory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dormitory> updateDormitory(@PathVariable Long id, @RequestBody DormitoryRequest updatedDormitory) {
        Dormitory dormitory = dormitoryService.updateDormitory(id, updatedDormitory);
        return ResponseEntity.ok(dormitory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDormitory(@PathVariable Long id) {
        dormitoryService.deleteDormitory(id);
        return ResponseEntity.ok("Dormitory deleted successfully");
    }


}