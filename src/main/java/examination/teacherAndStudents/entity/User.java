package examination.teacherAndStudents.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import examination.teacherAndStudents.utils.ContractType;
import examination.teacherAndStudents.utils.MaritalStatus;
import examination.teacherAndStudents.utils.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    //    @Size(min = 3, message = "Last name can not be less than 3")
        private String firstName;
    //    @Size(min = 3, message = "Last name can not be less than 3")
    private String lastName;
    private String middleName;

    private String email;
    //    @Size(min = 6, message = "Password should have at least 6 characters")
    private String password;
    @Enumerated(value = EnumType.STRING)
    private Roles roles;

    @Enumerated(value = EnumType.STRING)
    private MaritalStatus maritalStatus;

    //    @Size(min = 10, message = "Phone number should have at least 10 characters")
    private String phoneNumber;
    private LocalDate admissionDate;
    private Year year;
    private String uniqueRegistrationNumber;
    private String studentGuardianName;
    private String studentGuardianOccupation;
    private String profilePicture;
//    private String classAssigned;
    private String studentGuardianPhoneNumber;
    private String gender;

    private Boolean deactivate = false;

    private String religion;

    private Boolean isVerified;

    private String address;
    private Date dateOfBirth;
    private String age;

    private String formTeacher;

    private String bankAccountName;
    private String bankAccountNumber;
    private String bankName;
    private String resume;
    @Enumerated(value = EnumType.STRING)
    private ContractType contractType;
    private BigDecimal salary;
    private String academicQualification;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime modifiedAt;
    private String subjectAssigned;

    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;
    @JsonManagedReference
    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private Wallet wallet;


    @OneToMany(mappedBy = "user")
    private  List<Dues> duesList;


    @OneToMany(mappedBy = "user")
    private  List<StaffAttendancePercent> staffAttendancePercents;

    @OneToMany(mappedBy = "user")
    private  List<MedicalRecord> medicalRecords;


    @ManyToOne
    @JoinColumn(name = "student_id")
    private SubClass studentClass;

    @ManyToOne
    @JoinColumn(name = "hostel_id")
    private DormitoryRooms dormitory;

    @ManyToOne
    @JoinColumn(name = "transport_id")
    private Transport transport;

    @OneToMany(mappedBy = "user")
    private List<StaffAttendance> attendanceRecords;

    @OneToMany(mappedBy = "user")
    private List<Attendance> attendances;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", admissionDate=" + admissionDate +
                ", uniqueRegistrationNumber='" + uniqueRegistrationNumber + '\'' +
                ", studentGuardianName='" + studentGuardianName + '\'' +
                ", studentGuardianOccupation='" + studentGuardianOccupation + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", studentGuardianPhoneNumber='" + studentGuardianPhoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", religion='" + religion + '\'' +
                ", isVerified=" + isVerified +
                ", subjectAssigned='" + subjectAssigned + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", age='" + age + '\'' +
                ", formTeacher='" + formTeacher + '\'' +
                ", academicQualification='" + academicQualification + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }


}
