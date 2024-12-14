package examination.teacherAndStudents.dto;

import lombok.*;

import java.time.Year;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentListResponse {
    private String uniqueRegistrationNumber;
    private String name;

    private Long studentClassId;


//    private String studentClassCategoryId;
//    private String studentSubclassId;
//    private Year academicYear;
}
