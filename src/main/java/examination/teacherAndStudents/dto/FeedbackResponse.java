package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.entity.Feedback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeedbackResponse {

    private Long id;
    private String feedbackText;
    private LocalDateTime submittedTime;
    // Other fields...

    // Constructors, getters, and setters...

    public static FeedbackResponse fromFeedback(Feedback feedback) {
        FeedbackResponse response = new FeedbackResponse();
        response.setId(feedback.getId());
        response.setFeedbackText(feedback.getFeedbackText());
        response.setSubmittedTime(feedback.getSubmittedTime());
        // Map other fields...
        return response;
    }
}
