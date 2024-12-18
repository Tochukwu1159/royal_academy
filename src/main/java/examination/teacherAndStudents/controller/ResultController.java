package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.entity.Result;
import examination.teacherAndStudents.error_handler.EntityNotFoundException;
import examination.teacherAndStudents.service.PositionService;
import examination.teacherAndStudents.service.ResultService;
import examination.teacherAndStudents.utils.StudentTerm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/results")
public class ResultController {

    private final ResultService resultService;
    private final PositionService positionService;

    @GetMapping("/calculate/{classLevelId}/{studentId}/{term}/{subjectName}")
    public ResponseEntity<Result> calculateResult(@PathVariable Long classLevelId,@PathVariable Long studentId, @PathVariable String subjectName,  @PathVariable StudentTerm term, @PathVariable Year year) {
        try {
            // Assuming you have a method in the service to calculate the result
            Result result = resultService.calculateResult(classLevelId, studentId, subjectName,year, term);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
           throw new RuntimeException("Error calculating result: " + e.getMessage());
        }
    }

    @PostMapping("/update-average/{userId}/{classLevelId}/{term}")
    public ResponseEntity<String> calculateAverageResult(@PathVariable Long userId,@PathVariable Long classLevelId,@PathVariable Year year, @PathVariable StudentTerm term) {
        try {
            resultService.calculateAverageResult(userId, classLevelId,year, term);
            return ResponseEntity.ok("Average score updated successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating average score.");
        }
    }

    // You can add more endpoints to retrieve historical results or other related information
}
