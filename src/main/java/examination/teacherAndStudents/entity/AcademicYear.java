package examination.teacherAndStudents.entity;
import examination.teacherAndStudents.utils.AttendanceStatus;
import examination.teacherAndStudents.utils.StudentTerm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "academic_year")
public class AcademicYear extends BaseEntity{
    @Column(name = "academic_session")
    private String academicSession;
    @Column(name = "year")
    private Year year;

//    @OneToMany(mappedBy = "academicYear", cascade = CascadeType.ALL)
//    private List<ClassCategory> classCategories;
}