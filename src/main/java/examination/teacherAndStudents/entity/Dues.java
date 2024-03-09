package examination.teacherAndStudents.entity;

import examination.teacherAndStudents.utils.AvailabilityStatus;
import examination.teacherAndStudents.utils.DuesStatus;
import examination.teacherAndStudents.utils.StudentTerm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Year;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dues")
@Entity
@Builder
public class Dues {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String purpose;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private DuesStatus status;

    @Enumerated(EnumType.STRING)
    private StudentTerm term;
    private Year year;

    @Lob
    @Column(name = "receipt_photo")
    private byte[] receiptPhoto;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}