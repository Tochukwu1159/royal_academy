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

    private int numberOfStudents;
    private Year year;
    @ManyToOne
    @JoinColumn(name = "class_category_id")
    private ClassCategory classCategory;

//    @OneToMany(mappedBy = "classCategory", cascade = CascadeType.ALL)
//    private List<SubClass> subClasses;

    private String classUniqueUrl;

    @OneToMany(mappedBy = "subClass")
    private List<Subject> subject;

    @ManyToOne
    @JoinColumn(name = "form_teacher_id")
    private FormTeacher formTeacher;

    @OneToMany(mappedBy = "subClass")
    private  List<AttendancePercent> attendancePercents;

    @JsonBackReference
    @OneToMany(mappedBy = "studentClass")
    private List<StudentClassLevel> studentClassLevels = new ArrayList<>();


//    @OneToMany(mappedBy = "subClass", cascade = CascadeType.ALL)
//    private List<Attendance> attendanceList;

    @JsonBackReference
    @OneToMany(mappedBy = "subClass")
    private List<StudentRecord> studentRecords = new ArrayList<>();

//    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "subClasses",fetch = FetchType.EAGER)
//    @JsonIgnore
//    private Set<User> users;



//    @OneToMany(mappedBy = "subClass")
//    private List<Subject> subject;
//
//    @OneToMany(mappedBy = "subClass")
//    private List<User> user;
//
//    @OneToMany(mappedBy = "subClass")
//    private  List<Score> scores;
//
//    @OneToMany(mappedBy = "subClass")
//    private  List<Result> results;
//
//    @OneToMany(mappedBy = "subClass")
//    private  List<Position> positions;
//
//    @OneToMany(mappedBy = "subClass")
//    private  List<AttendancePercent> attendancePercents;
}

