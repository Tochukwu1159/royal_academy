package examination.teacherAndStudents.dto;

import lombok.Data;

@Data
public class ScoreDTO {
    private Long userId;
    private String name;
    private String subject;
    private int examScore;
    private int assessmentScore;
}