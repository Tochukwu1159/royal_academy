package examination.teacherAndStudents.controller;
import examination.teacherAndStudents.dto.HostelRequest;
import examination.teacherAndStudents.dto.MedicalRecordRequest;
import examination.teacherAndStudents.entity.Book;
import examination.teacherAndStudents.entity.BookBorrowing;
import examination.teacherAndStudents.entity.MedicalRecord;
import examination.teacherAndStudents.service.LibraryService;
import examination.teacherAndStudents.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping("/create")
    public ResponseEntity<MedicalRecord> createMedicalRecord(@PathVariable Long studentId, @RequestBody MedicalRecordRequest medicalRecordRequest) {
        MedicalRecord createdRecord = medicalRecordService.addMedicalRecord(studentId,medicalRecordRequest);
        return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable Long studentId, @RequestBody MedicalRecordRequest medicalRecordRequest) {
        MedicalRecord updatedRecord = medicalRecordService.updateMedicalRecord(studentId,medicalRecordRequest);
        return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MedicalRecord>> updateMedicalRecord(Long studentId) {
        List<MedicalRecord> getAllMedicalRecordsByStudent = medicalRecordService.getAllMedicalRecordsByStudent(studentId);
        return new ResponseEntity<>(getAllMedicalRecordsByStudent, HttpStatus.OK);
    }



    // Other methods for updating, retrieving, or deleting medical records can be added here
}