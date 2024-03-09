package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.FeedbackDto;
import examination.teacherAndStudents.dto.FeedbackResponse;
import examination.teacherAndStudents.entity.Feedback;
import examination.teacherAndStudents.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<FeedbackResponse>> getAllFeedback() {
        List<Feedback> feedbackList = feedbackService.getAllFeedback();

        // Map Feedback objects to FeedbackResponse objects
        List<FeedbackResponse> feedbackResponses = feedbackList.stream()
                .map(FeedbackResponse::fromFeedback)
                .collect(Collectors.toList());

        return ResponseEntity.ok(feedbackResponses);
    }
}
