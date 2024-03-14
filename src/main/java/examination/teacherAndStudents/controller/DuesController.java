package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.DuesRequest;
import examination.teacherAndStudents.entity.Dormitory;
import examination.teacherAndStudents.entity.Dues;
import examination.teacherAndStudents.service.DuesService;
import examination.teacherAndStudents.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dues")
public class DuesController {

    private final DuesService duesService;

    @Autowired
    public DuesController(DuesService duesService) {
        this.duesService = duesService;
    }

    @GetMapping("/all")
        public ResponseEntity<Page<Dues>> getAllDues(@RequestParam(defaultValue = AccountUtils.PAGENO) Integer pageNo,
                @RequestParam(defaultValue = AccountUtils.PAGESIZE) Integer pageSize,
                @RequestParam(defaultValue = "id") String sortBy) {
        Page<Dues> duesList = duesService.getAllDues(pageNo, pageSize, sortBy);
        return ResponseEntity.ok(duesList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dues> getDuesById(@PathVariable Long id) {
        Dues dues = duesService.getDuesById(id);
        if (dues != null) {
            return ResponseEntity.ok(dues);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Dues> createDues(@RequestBody DuesRequest duesRequest) {
        Dues createdDues = duesService.createDues(duesRequest);
        return ResponseEntity.ok(createdDues);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Dues> updateDues(@PathVariable Long id, @RequestBody DuesRequest updatedDues) {
        Dues updatedDuesResult = duesService.updateDues(id, updatedDues);
        if (updatedDuesResult != null) {
            return ResponseEntity.ok(updatedDuesResult);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDues(@PathVariable Long id) {
        boolean deleted = duesService.deleteDues(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}