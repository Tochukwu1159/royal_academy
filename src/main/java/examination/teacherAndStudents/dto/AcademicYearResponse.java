package examination.teacherAndStudents.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicYearResponse {
    private Long id;
    private Year year;
    private Long classLevelId;

    // Constructors, getters, and setters
}