package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.utils.DayOfWeek;
import examination.teacherAndStudents.utils.StudentTerm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class TimetableUpdateRequest {
    private Long schoolClassId;
    private DayOfWeek dayOfWeek;
    private List<SubjectScheduleRequest> subjectSchedules;
    private StudentTerm term;
    private Long yearId;
}
