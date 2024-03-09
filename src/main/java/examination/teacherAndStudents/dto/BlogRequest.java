package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.utils.StudentTerm;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class BlogRequest {
    private String SchoolEvent;
    private StudentTerm term;
    @Column(columnDefinition = "TEXT")
    private String eventDescription;
}
