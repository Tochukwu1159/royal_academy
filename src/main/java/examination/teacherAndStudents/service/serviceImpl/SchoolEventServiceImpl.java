package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.Security.SecurityConfig;
import examination.teacherAndStudents.dto.BlogRequest;
import examination.teacherAndStudents.entity.SchoolEvent;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.error_handler.AuthenticationFailedException;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.repository.SchoolEventRepository;
import examination.teacherAndStudents.repository.NotificationRepository;
import examination.teacherAndStudents.repository.TransactionRepository;
import examination.teacherAndStudents.repository.UserRepository;
import examination.teacherAndStudents.service.SchoolEventService;
import examination.teacherAndStudents.utils.Roles;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class SchoolEventServiceImpl implements SchoolEventService {

    private final SchoolEventRepository blogRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final TransactionRepository transactionRepository;

    public SchoolEventServiceImpl(SchoolEventRepository blogRepository, UserRepository userRepository,
                                  NotificationRepository notificationRepository,
                                  TransactionRepository transactionRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<SchoolEvent> getAllBlogPosts() {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new AuthenticationFailedException("Please login as an Admin");
            }
            return blogRepository.findAll();
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while fetching blog posts " + e.getMessage());
        }
    }

    public SchoolEvent getBlogPostById(Long id) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new AuthenticationFailedException("Please login as an Admin");
            }

            return blogRepository.findById(id)
                    .orElseThrow(() -> new CustomNotFoundException("Blog post not found with id: " + id));
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while fetching the blog post " + e.getMessage());
        }
    }


    public List<SchoolEvent> getEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        return blogRepository.findByEventDateBetween(startDate, endDate);
    }

    public SchoolEvent createBlogPost(BlogRequest blogPost) {
        try {
                String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new AuthenticationFailedException("Please login as an Admin");
            }
            SchoolEvent newBlog = new SchoolEvent();
            newBlog.setSchoolEvent(blogPost.getSchoolEvent());
            newBlog.setEventDescription(blogPost.getEventDescription());
            newBlog.setTerm(blogPost.getTerm());
            newBlog.setEventDate(LocalDate.now());
            return blogRepository.save(newBlog);
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while creating the blog post " +e.getMessage());
        }
    }

    public SchoolEvent updateBlogPost(Long id, BlogRequest updatedBlogPost) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new AuthenticationFailedException("Please login as an Admin");
            }

            SchoolEvent existingBlogPost = blogRepository.findById(id)
                    .orElseThrow(() -> new CustomNotFoundException("Blog post not found with id: " + id));

            existingBlogPost.setSchoolEvent(updatedBlogPost.getSchoolEvent());
            existingBlogPost.setEventDescription(updatedBlogPost.getEventDescription());
            existingBlogPost.setEventDate(LocalDate.now());
            // You can update other fields as needed

            return blogRepository.save(existingBlogPost);
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while updating the blog post" + e.getMessage());
        }
    }


    public boolean deleteBlogPost(Long id) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new AuthenticationFailedException("Please login as an Admin");
            }

            SchoolEvent existingBlogPost = blogRepository.findById(id)
                    .orElseThrow(() -> new CustomNotFoundException("Blog post not found with id: " + id));

            blogRepository.delete(existingBlogPost);
        } catch (Exception e) {
            throw new CustomInternalServerException("An error occurred while deleting the blog post " +  e.getMessage());
        }
        return false;
    }
}