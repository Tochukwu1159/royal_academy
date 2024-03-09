package examination.teacherAndStudents.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Table(name = "user_class")
    @Entity
    @Builder
    public class StudentClassLevel {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @JsonManagedReference
        @ManyToOne
        @JoinColumn(name = "subClass_id")
        private SubClass studentClass;

        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;
        private Boolean active = false;

//        @ManyToOne
//        @JoinColumn(name = "studentRecord_id")
//        private StudentRecord studentRecord;

    }
