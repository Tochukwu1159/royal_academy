package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.utils.Roles;
import examination.teacherAndStudents.utils.StudentTerm;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class NoticeRequest {
    private String title;
    private LocalDate noticeDate;
    private LocalDate publishedDate;

    private Roles role;
    @Column(columnDefinition = "TEXT")
    private String eventDescription;
}
