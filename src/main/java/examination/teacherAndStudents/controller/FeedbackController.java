package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.FeedbackDto;
import examination.teacherAndStudents.dto.FeedbackResponse;
import examination.teacherAndStudents.entity.Dues;
import examination.teacherAndStudents.entity.Feedback;
import examination.teacherAndStudents.service.FeedbackService;
import examination.teacherAndStudents.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Feedback>> getUserFeedbacks(@PathVariable Long userId) {
        List<Feedback> feedbacks = feedbackService.getUserFeedbacks(userId);
        return ResponseEntity.ok(feedbacks);
    }

    @PostMapping
    public ResponseEntity<Feedback> submitFeedback(@RequestBody FeedbackDto feedback) {
        Feedback submittedFeedback = feedbackService.submitFeedback(feedback);
        return ResponseEntity.ok(submittedFeedback);
    }

    @GetMapping("/all")
        public ResponseEntity<Page<FeedbackResponse>> getAllFeedback(@RequestParam(defaultValue = AccountUtils.PAGENO) Integer pageNo,
                @RequestParam(defaultValue = AccountUtils.PAGESIZE) Integer pageSize,
                @RequestParam(defaultValue = "id") String sortBy) {
        Page<Feedback> feedbackList = feedbackService.getAllFeedback(pageNo, pageSize, sortBy);

        // Map Feedback objects to FeedbackResponse objects
        Page<FeedbackResponse> feedbackResponses = feedbackList
                .map(FeedbackResponse::fromFeedback);

        return ResponseEntity.ok(feedbackResponses);
    }
}
