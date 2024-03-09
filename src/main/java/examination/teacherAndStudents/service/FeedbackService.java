package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.FeedbackDto;
import examination.teacherAndStudents.dto.ReplyFeedbackDto;
import examination.teacherAndStudents.entity.Feedback;

import java.util.List;

public interface FeedbackService {
    List<Feedback> getUserFeedbacks(Long userId);
    Feedback submitFeedback(FeedbackDto feedback);
    Feedback replyToFeedback(Long feedbackId, ReplyFeedbackDto replyDto);
    List<Feedback> getAllFeedback();
}
