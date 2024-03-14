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
public class AcademicYearRequest {
    private Year year;

    // Constructors, getters, and setters
}