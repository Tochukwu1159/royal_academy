package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.BlogRequest;
import examination.teacherAndStudents.entity.SchoolEvent;

import java.time.LocalDate;
import java.util.List;

public interface SchoolEventService {
    List<SchoolEvent> getAllBlogPosts();
    SchoolEvent getBlogPostById(Long id);
    List<SchoolEvent> getEventsByDateRange(LocalDate startDate, LocalDate endDate);
    SchoolEvent createBlogPost(BlogRequest blogPost);
    SchoolEvent updateBlogPost(Long id, BlogRequest updatedBlogPost);
    boolean deleteBlogPost(Long id);
}
