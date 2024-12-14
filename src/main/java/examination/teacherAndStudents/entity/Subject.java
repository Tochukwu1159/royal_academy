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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Table(name = "subject")
    @Entity
    @Builder
    public class Subject extends BaseEntity{
    private String name;

    private String asignedTeacher;

    @ManyToOne
    @JoinColumn(name = "subClass_id")
    private SubClass subClass;

    @Enumerated(EnumType.STRING)
    private StudentTerm term;

//        @OneToMany(mappedBy = "subject")
//    private  List<Score> scores;
}