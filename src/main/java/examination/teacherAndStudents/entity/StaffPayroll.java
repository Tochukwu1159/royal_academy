package examination.teacherAndStudents.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "staff_Payroll")
@Entity
@Builder
public class StaffPayroll extends BaseEntity{
    private String name;
    private  String uniqueRegistrationNumber;
    private double baseSalary;
    private double bonuses;
    private double deductions;
    private double netPay;

    private LocalDateTime datePayed;



    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private User staff;

}
