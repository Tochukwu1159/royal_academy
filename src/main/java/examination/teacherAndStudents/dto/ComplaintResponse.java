package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.entity.Complaint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComplaintResponse {

    private Long id;
    private String feedbackText;
    private LocalDateTime submittedTime;
    // Other fields...

    // Constructors, getters, and setters...

    public static ComplaintResponse fromComplaint(Complaint complaint) {
        ComplaintResponse response = new ComplaintResponse();
        response.setId(complaint.getId());
        response.setFeedbackText(complaint.getFeedbackText());
        response.setSubmittedTime(complaint.getSubmittedTime());
        // Map other fields...
        return response;
    }
}
