package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.entity.AcademicYear;
import examination.teacherAndStudents.utils.AttendanceStatus;
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
    private long studentUniqueId;
    private int assessemtScore;
    private int examScore;
    private long classLevelId;
    private long subjectId;




}
