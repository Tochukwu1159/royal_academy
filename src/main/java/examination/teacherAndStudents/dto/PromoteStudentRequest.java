package examination.teacherAndStudents.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromoteStudentRequest {
    private int currentAcademicYear;
    private int promotedAcademicYear;
    private Long classCategoryId;
    private Long currentClassCategoryId;
    private Long promotedClassCategoryId;
    private Long currentSubClassId;
    private Long promotedSubClassId;
    private double average;

    // Constructors, getters, and setters
}
