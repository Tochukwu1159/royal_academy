package examination.teacherAndStudents.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitorsResponse {
    private Long id;
    private String name;
    private String purpose;
    private LocalDateTime signIn;
    private LocalDateTime signOut;
}