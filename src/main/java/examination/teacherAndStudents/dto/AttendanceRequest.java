package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.utils.AttendanceStatus;
import examination.teacherAndStudents.utils.StudentTerm;
import lombok.Data;

import java.time.LocalDate;
import java.time.Year;

@Data
public class AttendanceRequest {
    private Long classLevelId;
    private StudentTerm studentTerm;

    private Long studentId;
    private Year year;

    private LocalDate date;
    private AttendanceStatus status;
}
