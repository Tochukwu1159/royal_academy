package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.ComplaintDto;
import examination.teacherAndStudents.dto.ComplaintResponse;
import examination.teacherAndStudents.entity.Complaint;
import examination.teacherAndStudents.service.ComplaintService;
import examination.teacherAndStudents.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feedbacks")
public class ComplaintController {


    private final ComplaintService feedbackService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Complaint>> getUserComplaints(@PathVariable Long userId) {
        List<Complaint> feedbacks = feedbackService.getUserComplaints(userId);
        return ResponseEntity.ok(feedbacks);
    }

    @PostMapping
    public ResponseEntity<Complaint> submitComplaint(@RequestBody ComplaintDto feedback) {
        Complaint submittedComplaint = feedbackService.submitComplaint(feedback);
        return ResponseEntity.ok(submittedComplaint);
    }

    @GetMapping("/all")
        public ResponseEntity<Page<ComplaintResponse>> getAllComplaint(@RequestParam(defaultValue = AccountUtils.PAGENO) Integer pageNo,
                @RequestParam(defaultValue = AccountUtils.PAGESIZE) Integer pageSize,
                @RequestParam(defaultValue = "id") String sortBy) {
        Page<Complaint> feedbackList = feedbackService.getAllComplaint(pageNo, pageSize, sortBy);

        // Map Complaint objects to ComplaintResponse objects
        Page<ComplaintResponse> feedbackResponses = feedbackList
                .map(ComplaintResponse::fromComplaint);

        return ResponseEntity.ok(feedbackResponses);
    }
}
