package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.BlogRequest;
import examination.teacherAndStudents.entity.SchoolEvent;
import examination.teacherAndStudents.service.SchoolEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/blog")
public class SchoolEventController {

    private final SchoolEventService blogService;

    @Autowired
    public SchoolEventController(SchoolEventService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<SchoolEvent>> getAllBlogPosts() {
        try {
            List<SchoolEvent> blogPosts = blogService.getAllBlogPosts();
            return ResponseEntity.ok(blogPosts);
        } catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<SchoolEvent> getBlogPostById(@PathVariable Long id) {
        try {
            SchoolEvent blogPost = blogService.getBlogPostById(id);
            if (blogPost != null) {
                return ResponseEntity.ok(blogPost);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<SchoolEvent>> getEventsByDateRange(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<SchoolEvent> events = blogService.getEventsByDateRange(start, end);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping("/posts")
    public ResponseEntity<SchoolEvent> createBlogPost(@RequestBody BlogRequest blogPost) {
        try {
            SchoolEvent createdBlogPost = blogService.createBlogPost(blogPost);
            return ResponseEntity.ok(createdBlogPost);
        } catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<SchoolEvent> updateBlogPost(@PathVariable Long id, @RequestBody BlogRequest updatedBlogPost) {
        try {
            SchoolEvent updatedPost = blogService.updateBlogPost(id, updatedBlogPost);
            if (updatedPost != null) {
                return ResponseEntity.ok(updatedPost);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable Long id) {
        try {
            boolean deleted = blogService.deleteBlogPost(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.status(500).build();
        }
    }
}