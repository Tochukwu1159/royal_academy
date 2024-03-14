package examination.teacherAndStudents.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSummaryResponse {
    private long totalStudents;
    private long totalTeachers;
    private long totalParents;
    private long totalDrivers;
    private long totalGatemen;
    private long totalAdmins;
    private long totalLibrarians;
}