package examination.teacherAndStudents.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Table(name = "c")
public class SubClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subClassName;

    private int numberOfStudents = 0;
    @ManyToOne
    @JoinColumn(name = "class_category_id")
    private ClassCategory classCategory;


    private String classUniqueUrl;

    @OneToMany(mappedBy = "subClass")
    private List<Subject> subject;

    @OneToOne
    @JoinColumn(name = "form_teacher_id")
    private FormTeacher formTeacher;

    @OneToMany(mappedBy = "subClass")
    private  List<AttendancePercent> attendancePercents;

    @OneToMany(mappedBy = "studentClass", cascade = CascadeType.ALL)
    private List<User> students;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private ClassCategory studentClassLevels;



    @OneToMany(mappedBy = "subClass", cascade = CascadeType.ALL)
    private List<Attendance> attendanceList;



    @OneToMany(mappedBy = "subClass")
    private  List<Result> results;

    @OneToMany(mappedBy = "subClass")
    private  List<Position> positions;

}

