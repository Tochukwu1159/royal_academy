package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.utils.DuesStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DuesUpdateRequest {
    private String purpose;
    private BigDecimal amount;
    private DuesStatus status;
}
