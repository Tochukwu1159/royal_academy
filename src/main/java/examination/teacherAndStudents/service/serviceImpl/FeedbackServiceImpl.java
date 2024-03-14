package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.Security.SecurityConfig;
import examination.teacherAndStudents.dto.FeedbackDto;
import examination.teacherAndStudents.dto.ReplyFeedbackDto;
import examination.teacherAndStudents.entity.Dues;
import examination.teacherAndStudents.entity.Feedback;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.error_handler.NotFoundException;
import examination.teacherAndStudents.repository.FeedbackRepository;
import examination.teacherAndStudents.repository.UserRepository;
import examination.teacherAndStudents.service.FeedbackService;
import examination.teacherAndStudents.utils.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {


    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Feedback> getUserFeedbacks(Long userId) {
        return feedbackRepository.findByUserId(userId);
    }

    public Feedback submitFeedback(FeedbackDto feedback) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User user = userRepository.findByEmailAndRoles(email, Roles.STUDENT);
            if (user == null) {
                throw new CustomNotFoundException("Please login as a Student"); // Return unauthorized response for non-admin users
            }
            Feedback newFeed = new Feedback();
            newFeed.setFeedbackText(feedback.getFeedbackText());
            newFeed.setSubmittedTime(LocalDateTime.now());
            newFeed.setUser(user);
            return feedbackRepository.save(newFeed);
        } catch (Exception e) {
            throw new CustomInternalServerException("Error sending feedback: " + e.getMessage());
        }
    }


    public Feedback replyToFeedback(Long feedbackId, ReplyFeedbackDto replyFeedbackDto) {
        try {
            // Find the feedback by ID
            Feedback feedback = feedbackRepository.findById(feedbackId)
                    .orElseThrow(() -> new CustomNotFoundException("Feedback not found"));

            // Check if the authenticated user is an admin
            String adminEmail = SecurityConfig.getAuthenticatedUserEmail();
            User adminUser = userRepository.findByEmailAndRoles(adminEmail, Roles.ADMIN);
            if (adminUser == null) {
                throw new CustomNotFoundException("Please login as an Admin");
            }

            // Set the reply text and update the submitted time
            feedback.setReplyText(replyFeedbackDto.getReplyText());
            feedback.setReplyTime(LocalDateTime.now());

            // Save the updated feedback
            return feedbackRepository.save(feedback);
        } catch (Exception e) {
            throw new CustomInternalServerException("Error replying to feedback: " + e.getMessage());
        }
    }

       public Page<Feedback> getAllFeedback(int pageNo, int pageSize, String sortBy){
            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        return feedbackRepository.findAll(paging);
    }

}