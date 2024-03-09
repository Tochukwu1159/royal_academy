package examination.teacherAndStudents.entity;
import examination.teacherAndStudents.utils.TeachingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subject_schedule")
@Entity
@Builder
public class SubjectSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "timetable_id")
    private Timetable timetable;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String topic;
    private LocalDateTime teachersUpdatedTime;

    @Enumerated(EnumType.STRING)
    private TeachingStatus teachingStatus;


    // Getters and setters
}