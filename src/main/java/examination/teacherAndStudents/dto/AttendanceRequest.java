package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.utils.AttendanceStatus;
import examination.teacherAndStudents.utils.StudentTerm;
import lombok.Data;

import java.time.LocalDate;
import java.time.Year;

@Data
public class AttendanceRequest {

    private long studentUniqueId;

    private AttendanceStatus status;
    private Long studentClassId;
}


