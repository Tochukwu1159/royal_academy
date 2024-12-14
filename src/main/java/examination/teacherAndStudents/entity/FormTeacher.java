package examination.teacherAndStudents.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "formTeacher")
@Entity
@Builder
public class FormTeacher extends BaseEntity {

    @OneToOne(mappedBy = "formTeacher")
    private SubClass subClass;

    private Year year;
    private Boolean active;

}
