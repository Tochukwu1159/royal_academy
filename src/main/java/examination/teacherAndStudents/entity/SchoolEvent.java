package examination.teacherAndStudents.entity;

import examination.teacherAndStudents.utils.StudentTerm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Year;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schoolEvent")
@Entity
@Builder
public class SchoolEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String SchoolEvent;

    @Column(nullable = false)
    private LocalDate eventDate;

    private String eventDescription;

    @Enumerated(EnumType.STRING)
    private StudentTerm term;

//    @ManyToOne
//    @JoinColumn(name = "student_id")
//    private User user;
}
