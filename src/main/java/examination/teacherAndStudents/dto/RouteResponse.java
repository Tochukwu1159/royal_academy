package examination.teacherAndStudents.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponse {
    private Long id;
    private String routeName;
    private String startPoint;
    private String endPoint;

    // Constructors, getters, and setters
}