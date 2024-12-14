package examination.teacherAndStudents.entity;

import examination.teacherAndStudents.utils.AttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "staff_attendance")
public class StaffAttendance extends BaseEntity{
    @Column(nullable = false)
    private String staffUniqueRegNumber;

    @Column(nullable = false)
    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private String checkInLocation;

    private String checkOutLocation;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status; // Enum for present or absent

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    // Constructors, getters, and setters

}

