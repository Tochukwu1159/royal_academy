package examination.teacherAndStudents.entity;

import com.fasterxml.jackson.databind.annotation.EnumNaming;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StudyMaterial extends BaseEntity {
    private String title;
    private String filePath; // Path to the stored PDF file
    private Long courseId; // ID of the associated course
    private Long teacherId; // ID of the teacher who uploaded the material
    // Other fields, getters, setters
}