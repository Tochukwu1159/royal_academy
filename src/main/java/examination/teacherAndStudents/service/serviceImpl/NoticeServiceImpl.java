package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.Security.SecurityConfig;
import examination.teacherAndStudents.dto.NoticeRequest;
import examination.teacherAndStudents.dto.UpdateNoticeRequest;
import examination.teacherAndStudents.entity.Notice;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.error_handler.AuthenticationFailedException;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.repository.NoticeRepository;
import examination.teacherAndStudents.repository.NotificationRepository;
import examination.teacherAndStudents.repository.TransactionRepository;
import examination.teacherAndStudents.repository.UserRepository;
import examination.teacherAndStudents.service.NoticeService;
import examination.teacherAndStudents.utils.Roles;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final TransactionRepository transactionRepository;

    public NoticeServiceImpl(NoticeRepository noticeRepository, UserRepository userRepository,
                             NotificationRepository notificationRepository,
                             TransactionRepository transactionRepository) {
        this.noticeRepository = noticeRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Notice> getAllNoticePosts() {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new AuthenticationFailedException("Please login as an Admin");
            }
            return noticeRepository.findAll();
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while fetching blog posts " + e.getMessage());
        }
    }

    public Notice getNoticePostById(Long id) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new AuthenticationFailedException("Please login as an Admin");
            }

            return noticeRepository.findById(id)
                    .orElseThrow(() -> new CustomNotFoundException("Notice post not found with id: " + id));
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while fetching the blog post " + e.getMessage());
        }
    }


    public List<Notice> getEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        return noticeRepository.findByEventDateBetween(startDate, endDate);
    }

    public Notice createNoticePost(NoticeRequest noticeRequest) {
        try {
                String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new AuthenticationFailedException("Please login as an Admin");
            }
            Notice newNotice = new Notice();
            newNotice.setTitle(noticeRequest.getTitle());
            newNotice.setEventDescription(noticeRequest.getEventDescription());
            newNotice.setPublishedDate(LocalDate.now());
            newNotice.setNoticeDate(noticeRequest.getNoticeDate());
            newNotice.setRoles(noticeRequest.getRole());
            newNotice.setEventDate(LocalDate.now());
            return noticeRepository.save(newNotice);
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while creating the blog post " +e.getMessage());
        }
    }

    public Notice updateNoticePost(Long id, UpdateNoticeRequest updatedNoticePost) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new AuthenticationFailedException("Please login as an Admin");
            }

            Notice existingNoticePost = noticeRepository.findById(id)
                    .orElseThrow(() -> new CustomNotFoundException("Notice post not found with id: " + id));

            existingNoticePost.setTitle(updatedNoticePost.getTitle());
            existingNoticePost.setRoles(updatedNoticePost.getRole());
            existingNoticePost.setUpdateNoticeDate(LocalDate.now());
            existingNoticePost.setPublishedDate(updatedNoticePost.getPublishedDate());
            existingNoticePost.setEventDescription(updatedNoticePost.getEventDescription());
            existingNoticePost.setEventDate(LocalDate.now());
            // You can update other fields as needed

            return noticeRepository.save(existingNoticePost);
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while updating the blog post" + e.getMessage());
        }
    }


    public boolean deleteNoticePost(Long id) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new AuthenticationFailedException("Please login as an Admin");
            }

            Notice existingNoticePost = noticeRepository.findById(id)
                    .orElseThrow(() -> new CustomNotFoundException("Notice post not found with id: " + id));

            noticeRepository.delete(existingNoticePost);
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while deleting the blog post " +  e.getMessage());
        }
        return false;
    }

}