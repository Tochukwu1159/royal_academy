package examination.teacherAndStudents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherStaffRequest {

    private String firstName;
    private String address;
    private String phoneNumber;
    private String lastName;
    private String jobDescription;

// Constructors, getters, and setters
}
