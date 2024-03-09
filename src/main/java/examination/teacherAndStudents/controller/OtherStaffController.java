package examination.teacherAndStudents.controller;
import examination.teacherAndStudents.dto.OtherStaffRequest;
import examination.teacherAndStudents.dto.OtherStaffResponse;
import examination.teacherAndStudents.service.OtherStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/other-staff")
public class OtherStaffController {

    @Autowired
    private OtherStaffService otherStaffService;

    @PostMapping("/create")
    public ResponseEntity<OtherStaffResponse> createOtherStaff(@RequestBody OtherStaffRequest otherStaffRequest) {
        OtherStaffResponse createdOtherStaff = otherStaffService.createOtherStaff(otherStaffRequest);
        return new ResponseEntity<>(createdOtherStaff, HttpStatus.CREATED);
    }

    @PutMapping("/update/{otherStaffId}")
    public ResponseEntity<OtherStaffResponse> updateOtherStaff(
            @PathVariable Long otherStaffId,
            @RequestBody OtherStaffRequest updatedOtherStaff) {
        OtherStaffResponse updatedOtherStaffResponse = otherStaffService.updateOtherStaff(otherStaffId, updatedOtherStaff);
        return new ResponseEntity<>(updatedOtherStaffResponse, HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<OtherStaffResponse>> findAllOtherStaff() {
        List<OtherStaffResponse> allOtherStaff = otherStaffService.findAllOtherStaff();
        return new ResponseEntity<>(allOtherStaff, HttpStatus.OK);
    }

    @GetMapping("/findById/{otherStaffId}")
    public ResponseEntity<OtherStaffResponse> findOtherStaffById(@PathVariable Long otherStaffId) {
        OtherStaffResponse otherStaffById = otherStaffService.findOtherStaffById(otherStaffId);
        return new ResponseEntity<>(otherStaffById, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{otherStaffId}")
    public ResponseEntity<Void> deleteOtherStaff(@PathVariable Long otherStaffId) {
        otherStaffService.deleteOtherStaff(otherStaffId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
