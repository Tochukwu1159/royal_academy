package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.utils.DuesStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DuesResponse {
    private Long id;
    private String purpose;
    private BigDecimal amount;
    private DuesStatus status;
    // Add other necessary fields and constructors/getters/setters
}