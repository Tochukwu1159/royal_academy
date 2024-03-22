package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.DormitoryRequest;
import examination.teacherAndStudents.dto.DormitoryResponse;
import examination.teacherAndStudents.dto.DormitoryRoomResponse;
import examination.teacherAndStudents.entity.Dormitory;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.error_handler.InsufficientBalanceException;
import examination.teacherAndStudents.error_handler.NotFoundException;
import examination.teacherAndStudents.service.DormitoryService;
import examination.teacherAndStudents.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dormitories")
public class DormitoryController {


    private final DormitoryService dormitoryService;

    @GetMapping("/all")
        public ResponseEntity<Page<DormitoryResponse>> getAllDormitorys(@RequestParam(defaultValue = AccountUtils.PAGENO) Integer pageNo,
                                                                        @RequestParam(defaultValue = AccountUtils.PAGESIZE) Integer pageSize,
                                                                        @RequestParam(defaultValue = "id") String sortBy) {
        Page<DormitoryResponse> dormitories = dormitoryService.getAllDormitorys(pageNo, pageSize, sortBy);
        return ResponseEntity.ok(dormitories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DormitoryResponse> getDormitoryById(@PathVariable Long id) {
        Optional<DormitoryResponse> dormitory = dormitoryService.getDormitoryById(id);
        return ResponseEntity.ok(dormitory.get());
    }

    @PostMapping("/create")
    public ResponseEntity<DormitoryResponse> createDormitory(@RequestBody DormitoryRequest dormitory) {
        DormitoryResponse createdDormitory = dormitoryService.createDormitory(dormitory);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDormitory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DormitoryResponse> updateDormitory(@PathVariable Long id, @RequestBody DormitoryRequest updatedDormitory) {
        DormitoryResponse dormitory = dormitoryService.updateDormitory(id, updatedDormitory);
        return ResponseEntity.ok(dormitory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDormitory(@PathVariable Long id) {
        dormitoryService.deleteDormitory(id);
        return ResponseEntity.ok("Dormitory deleted successfully");
    }


}