package examination.teacherAndStudents.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FeeGroupResponse {
    private Long id;
    private String name;
    private double amount;
    private String description;

    // Constructors, getters, and setters


    // Getters and setters
}
