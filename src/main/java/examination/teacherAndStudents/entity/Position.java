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
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "position")
@Entity
@Builder
    public class Position extends BaseEntity{
        private double averageScore;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
    @Column(name = "position_rank")
    private int positionRank;

    @ManyToOne
    @JoinColumn(name = "classLel_id")
    @JsonBackReference
    private SubClass subClass;

    @Enumerated(EnumType.STRING)
    private StudentTerm term;

    }
