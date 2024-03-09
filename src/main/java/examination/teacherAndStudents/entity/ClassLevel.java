//package examination.teacherAndStudents.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "class_level")
//@Entity
//@Builder
//public class ClassLevel {
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long id;
////    private String className;
////    private String formTeacher;
////    private String classUniqueUrl;
////    @Column(columnDefinition = "int default 0")
////    private int numberOfStudents;
//
//
////    @OneToMany(mappedBy = "classLevel")
////    private List<User> user;
//
////    @OneToMany(mappedBy = "classLevel")
////    private  List<Score> scores;
//
////    @OneToMany(mappedBy = "classLevel")
////    private  List<Result> results;
//
////    @OneToMany(mappedBy = "classLevel")
////    private  List<Position> positions;
//
//    @OneToMany(mappedBy = "classLevel")
//    private  List<AttendancePercent> attendancePercents;
//
//
//    @Override
//    public String toString() {
//        // Use a controlled approach to print relevant information
//        return "ClassLevel{" +
//                "id=" + id +
//                ", className='" + className + '\'' +
//                ", formTeacher='" + formTeacher + '\'' +
//                ", numberOfStudents=" + numberOfStudents +
//                '}';
//    }
//
//
//
//}
