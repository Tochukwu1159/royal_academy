package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.FeedbackDto;
import examination.teacherAndStudents.dto.ReplyFeedbackDto;
import examination.teacherAndStudents.entity.Feedback;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FeedbackService {
    List<Feedback> getUserFeedbacks(Long userId);
    Feedback submitFeedback(FeedbackDto feedback);
    Feedback replyToFeedback(Long feedbackId, ReplyFeedbackDto replyDto);
    Page<Feedback> getAllFeedback(int pageNo, int pageSize, String sortBy);
}
