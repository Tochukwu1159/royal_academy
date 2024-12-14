package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.utils.AttendanceStatus;
import examination.teacherAndStudents.utils.StudentTerm;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentListRequest {
    private long classLevelId;
    private long subClassId;

    private long academicYearId;

}
