package examination.teacherAndStudents.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import examination.teacherAndStudents.utils.StudentTerm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "result")
@Entity
@Builder
public class Result extends BaseEntity{

    private Double totalMarks;
    private String subjectName;
    private String grade;
    private String rating;
    @Enumerated(EnumType.STRING)
    private StudentTerm term;


    @ManyToOne
    @JoinColumn(name = "classLevel_id")
    @JsonBackReference
    private SubClass subClass;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
