package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.entity.SubClass;
import examination.teacherAndStudents.utils.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Year;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @NotNull(message = "First Name is required")
    @NotEmpty(message = "first name cannot be empty")
    private String firstName;
    private String religion;
    @NotNull(message = "Last Name is required")
    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;
    private LocalDate admissionDate;
    private String studentGuardianOccupation;

    @NotNull(message = "Password is required")
    @NotEmpty(message = "Password cannot be empty")
    private  String password;
    @NotNull(message = "Confirm Password is required")
    @NotEmpty(message = "Confirm Password cannot be empty")
    private String confirmPassword;
    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    private Year year;
    @NotNull(message = "Phone Number is required")
    @NotEmpty(message = "Phone Number cannot be empty")
    private String phoneNumber;
     private String registrationNumber;
    private Date dateOfBirth;
    private String formTeacher;
    private SubClass classAssigned;
    private  String address;
    private String age;
    private Long classCategoryId;
    private Long subClassId;

    private String studentGuardianName;
    private String studentGuardianPhoneNumber;
    private String subjectAssigned;
    private String academicQualification;




    @NotNull(message = "Gender is required")
    @NotEmpty(message = "Gender  cannot be empty")
    private Gender gender;
}
