package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.utils.AttendanceStatus;
import examination.teacherAndStudents.utils.StudentTerm;
import lombok.Data;

import java.time.LocalDate;
import java.time.Year;

@Data
public class AttendanceRequest {
    private long classLevelId;
    private long subClassId;

    private StudentTerm studentTerm;

    private long studentId;
    private long academicYearId;

    private LocalDate date;
    private AttendanceStatus status;
}


