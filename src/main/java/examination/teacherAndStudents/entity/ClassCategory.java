package examination.teacherAndStudents.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassCategory extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String categoryName;

    @OneToMany(mappedBy = "studentClassLevels", cascade = CascadeType.ALL)
    private List<SubClass> subClasses;

    @ManyToOne
    @JoinColumn(name = "academic_year_id")
    private AcademicYear academicYear;

}

