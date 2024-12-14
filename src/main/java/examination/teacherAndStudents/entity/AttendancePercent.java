package examination.teacherAndStudents.entity;

import lombok.AllArgsConstructor;
import examination.teacherAndStudents.utils.AttendanceStatus;
import examination.teacherAndStudents.utils.StudentTerm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attendance_percent")
@Entity
@Builder
public class AttendancePercent extends BaseEntity {

    private Double attendancePercentage;

    @Enumerated(EnumType.STRING)
    private StudentTerm studentTerm;

    private Year year;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "subclass_id", nullable = false)
    private SubClass subClass;

}