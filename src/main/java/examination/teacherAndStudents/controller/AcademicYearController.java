package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.AcademicYearRequest;
import examination.teacherAndStudents.dto.AcademicYearResponse;
import examination.teacherAndStudents.service.AcademicYearService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/academic-years")
public class AcademicYearController {

    private final AcademicYearService academicYearService;

    public AcademicYearController(AcademicYearService academicYearService) {
        this.academicYearService = academicYearService;
    }

    @PostMapping("/create")
    public ResponseEntity<AcademicYearResponse> createAcademicYear(@RequestBody AcademicYearRequest request) {
        AcademicYearResponse response = academicYearService.createAcademicYear(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcademicYearResponse> editAcademicYear(@PathVariable Long id, @RequestBody AcademicYearRequest request) {
        AcademicYearResponse response = academicYearService.editAcademicYear(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<AcademicYearResponse>> findAllAcademicYears(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<AcademicYearResponse> academicYearPage = academicYearService.findAllAcademicYears(pageNo, pageSize);
        return ResponseEntity.ok(academicYearPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicYearResponse> findAcademicYearById(@PathVariable Long id) {
        AcademicYearResponse response = academicYearService.findAcademicYearById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAcademicYear(@PathVariable Long id) {
        academicYearService.deleteAcademicYear(id);
        return ResponseEntity.noContent().build();
    }
}
