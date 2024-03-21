package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.NoticeRequest;
import examination.teacherAndStudents.dto.UpdateNoticeRequest;
import examination.teacherAndStudents.entity.Notice;

import java.time.LocalDate;
import java.util.List;

public interface NoticeService {
    List<Notice> getAllNoticePosts();
    Notice getNoticePostById(Long id);
    List<Notice> getEventsByDateRange(LocalDate startDate, LocalDate endDate);
    Notice createNoticePost(NoticeRequest blogPost);
    Notice updateNoticePost(Long id, UpdateNoticeRequest updatedNoticePost) ;
    boolean deleteNoticePost(Long id);
}
