package examination.teacherAndStudents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherStaffResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String jobDescription;
    private String address;
    private String phoneNumber;
}
