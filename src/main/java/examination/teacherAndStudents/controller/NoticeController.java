package examination.teacherAndStudents.controller;


import examination.teacherAndStudents.dto.NoticeRequest;
import examination.teacherAndStudents.dto.UpdateNoticeRequest;
import examination.teacherAndStudents.entity.Notice;
import examination.teacherAndStudents.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notice")
public class NoticeController {

    private final NoticeService blogService;

    @Autowired
    public NoticeController(NoticeService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Notice>> getAllNoticePosts() {
        try {
            List<Notice> blogPosts = blogService.getAllNoticePosts();
            return ResponseEntity.ok(blogPosts);
        } catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNoticePostById(@PathVariable Long id) {
        try {
            Notice blogPost = blogService.getNoticePostById(id);
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
    public ResponseEntity<List<Notice>> getEventsByDateRange(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<Notice> events = blogService.getEventsByDateRange(start, end);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Notice> createNoticePost(@RequestBody NoticeRequest blogPost) {
        try {
            Notice createdNoticePost = blogService.createNoticePost(blogPost);
            return ResponseEntity.ok(createdNoticePost);
        } catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notice> updateNoticePost(@PathVariable Long id, @RequestBody UpdateNoticeRequest updatedNoticePost) {
        try {
            Notice updatedPost = blogService.updateNoticePost(id, updatedNoticePost);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoticePost(@PathVariable Long id) {
        try {
            boolean deleted = blogService.deleteNoticePost(id);
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