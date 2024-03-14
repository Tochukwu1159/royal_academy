package examination.teacherAndStudents.controller;
import examination.teacherAndStudents.dto.StaffRequest;
import examination.teacherAndStudents.dto.StaffResponse;
import examination.teacherAndStudents.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/-staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @PostMapping("/create")
    public ResponseEntity<StaffResponse> createStaff(@RequestBody StaffRequest staffRequest) {
        StaffResponse createdStaff = staffService.createStaff(staffRequest);
        return new ResponseEntity<>(createdStaff, HttpStatus.CREATED);
    }

    @PutMapping("/update/{StaffId}")
    public ResponseEntity<StaffResponse> updateStaff(
            @PathVariable Long staffId,
            @RequestBody StaffRequest updatedStaff) {
        StaffResponse updatedStaffResponse = staffService.updateStaff(staffId, updatedStaff);
        return new ResponseEntity<>(updatedStaffResponse, HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<StaffResponse>> findAllStaff() {
        List<StaffResponse> allStaff = staffService.findAllStaff();
        return new ResponseEntity<>(allStaff, HttpStatus.OK);
    }

    @GetMapping("/findById/{StaffId}")
    public ResponseEntity<StaffResponse> findStaffById(@PathVariable Long StaffId) {
        StaffResponse staffById = staffService.findStaffById(StaffId);
        return new ResponseEntity<>(staffById, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{StaffId}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long StaffId) {
        staffService.deleteStaff(StaffId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
