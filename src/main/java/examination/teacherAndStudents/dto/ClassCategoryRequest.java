package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.entity.AcademicYear;
import lombok.Data;

import java.time.Year;

@Data
public class ClassCategoryRequest {
    private String categoryName;
    private long academicYearId;

}
