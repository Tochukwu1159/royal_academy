package examination.teacherAndStudents.entity;

import examination.teacherAndStudents.utils.StudentTerm;
import examination.teacherAndStudents.utils.TimetableType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "transaction")
public class Transaction extends BaseEntity{
    private String transactionType;
    private String description;
    private BigDecimal amount;

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private StudentTerm term;

    private Year year;


}
