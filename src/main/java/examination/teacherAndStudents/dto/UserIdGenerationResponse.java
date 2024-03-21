package examination.teacherAndStudents.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class UserIdGenerationResponse {
        private String photoUrl;
        private String firstName;
        //    @Size(min = 3, message = "Last name can not be less than 3")
        private String lastName;
        private LocalDate admissionDate;
        private String studentGuardianOccupation;


        private String email;
        private String phoneNumber;
        private String uniqueRegistrationNumber;
        private String studentGuardianName;
        private Date dateOfBirth;
        private String classAssigned;
        private String studentGuardianPhoneNumber;
        private String gender;
        private String subjectAssigned;
        private String address;
        private String academicQualification;
    }

