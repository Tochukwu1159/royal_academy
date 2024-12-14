package examination.teacherAndStudents.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "wallet")
public class Wallet  extends  BaseEntity{

    @DecimalMin(value = "0.00", inclusive = true) // Allow zero values
    @Digits(integer = 9, fraction = 2)
    @Column(
            name = "balance",
            nullable = false,
            columnDefinition = "NUMERIC(11,2) DEFAULT 0.0"
    )
    private BigDecimal balance;
    @Column(
            name = "totalMoneySent",
            nullable = false,
            columnDefinition = "NUMERIC(11,2) DEFAULT 0.0"
    )
    private BigDecimal totalMoneySent;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Profile userProfile;

}
