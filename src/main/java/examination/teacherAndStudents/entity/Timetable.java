package examination.teacherAndStudents.entity;
import examination.teacherAndStudents.utils.DayOfWeek;
import examination.teacherAndStudents.utils.StudentTerm;
import examination.teacherAndStudents.utils.TimetableType;
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
@Table(name = "timetable")
@Entity
@Builder
public class Timetable extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "class_id")
    private SubClass subClass;
    @Enumerated(EnumType.STRING)
    private TimetableType timetableType;

    @Enumerated(EnumType.STRING)
    private StudentTerm term;

    private Year year;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
}
