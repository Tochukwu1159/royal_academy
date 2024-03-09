package examination.teacherAndStudents.controller;

import com.itextpdf.text.DocumentException;
import examination.teacherAndStudents.entity.SubClass;
import examination.teacherAndStudents.error_handler.EntityNotFoundException;
import examination.teacherAndStudents.service.PositionService;
import examination.teacherAndStudents.utils.StudentTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/api/v1/position")
public class PositionController {

    @Autowired
    private PositionService positionService;


    @GetMapping("/update-all/{classLevelId}/{term}")
    public ResponseEntity<String> updateAllPositionsForAClass(@PathVariable SubClass studentClass, @PathVariable Year year, @PathVariable StudentTerm term) {
        try {
            positionService.updateAllPositionsForAClass(studentClass, year,term);
            return ResponseEntity.ok("Positions updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating positions: " + e.getMessage());
        }
    }

    @PostMapping("/generateResultSummaryPdf/{studentId}/{classLevelId}/{term}")
    public ResponseEntity<String> generateResultSummaryPdf(@PathVariable Long studentId, @PathVariable Long classLevelId,@PathVariable Year year, @PathVariable StudentTerm term) {
        try {
            positionService.generateResultSummaryPdf(studentId,classLevelId, year, term);
            return ResponseEntity.ok("Result summary PDF generation initiated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating result summary PDF: " + e.getMessage());
        }
    }
}
