package examination.teacherAndStudents.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.utils.ScoreType;
import examination.teacherAndStudents.utils.SickLeaveStatus;
import examination.teacherAndStudents.utils.StudentTerm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Year;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sick_leave")
@Entity
@Builder
public class SickLeave extends BaseEntity{


    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User user;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    private Boolean Cancelled;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SickLeaveStatus status;

    private Year year;

    @Enumerated(EnumType.STRING)
    private StudentTerm term;

    // Constructors, getters, and setters

    // Additional methods or fields if needed
}

