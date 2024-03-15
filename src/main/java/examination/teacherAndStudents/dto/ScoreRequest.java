package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.entity.AcademicYear;
import examination.teacherAndStudents.utils.StudentTerm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScoreRequest {
    private long subClassId;
    private  Long studentId;
    private Long subjectId;
    private int examScore;
    private int assessmentScore;
    private Long academicYearId;;
    private StudentTerm term;


}
