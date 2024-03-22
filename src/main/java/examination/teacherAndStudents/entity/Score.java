package examination.teacherAndStudents.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.utils.ScoreType;
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
@Table(name = "score")
@Entity
@Builder
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private int examScore;
    private int assessmentScore;
    private String subjectName;
    @ManyToOne
    @JoinColumn(name = "classLevel_id")
    @JsonBackReference
    private Subject subject;

    @Enumerated(EnumType.STRING)
    private StudentTerm term;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;





    // Constructors, getters, and setters
}
