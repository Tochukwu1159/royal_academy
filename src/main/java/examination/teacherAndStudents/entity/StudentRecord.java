package examination.teacherAndStudents.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StudentRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "int default 0")
    private int numberOfStudents;
    private String year;
    private String formTeacher;
    @ManyToOne
    @JoinColumn(name = "subClass_id")
    private SubClass subClass;


    //    @OneToMany(mappedBy = "classLevel")
//    private  List<AttendancePercent> attendancePercents;
//    @OneToMany(mappedBy = "subClass", cascade = CascadeType.ALL)
//    private List<Attendance> attendanceList;

//    @JsonBackReference
//    @OneToMany(mappedBy = "subClass")
//    private List<StudentRecord> studentRecords = new ArrayList<>();

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
