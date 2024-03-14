package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.entity.ClassCategory;
import lombok.Data;

import java.time.Year;

@Data
public class SubClassRequest {
    private Long categoryId;
    private String subClassName;
    private int numberOfStudents;
    private Year year;
    private String classUniqueUrl;
}
