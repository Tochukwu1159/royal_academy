package examination.teacherAndStudents.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class WalletResponse {
    private BigDecimal balance;
    private BigDecimal totalMoneySent;
}
