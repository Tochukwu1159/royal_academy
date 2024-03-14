package examination.teacherAndStudents.dto;

import lombok.Data;

import java.time.Year;

@Data
public class SubClassResponse {
    private Long id;
    private String subClassName;
    private int numberOfStudents;
    private Year year;
    private String classUniqueUrl;

}
