package examination.teacherAndStudents.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import examination.teacherAndStudents.utils.ContractType;
import examination.teacherAndStudents.utils.Gender;
import examination.teacherAndStudents.utils.MaritalStatus;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Profile")
@Entity
@Builder
public class Profile extends BaseEntity{


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
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String religion;


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

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Wallet wallet;

//    @OneToMany(mappedBy = "user")
//    private List<Transaction> transactions;


//    @OneToMany(mappedBy = "user")
//    private  List<Dues> duesList;
//
//    @OneToMany(mappedBy = "user")
//    private  List<Result> resultList;
//
//    @OneToMany(mappedBy = "user")
//    private  List<Score> scoreList;
//
//    @OneToMany(mappedBy = "staff")
//    private List<StaffPayroll> staffPayrolls;
//
//
//    @OneToMany(mappedBy = "user")
//    private  List<StaffAttendancePercent> staffAttendancePercents;
//
//    @OneToMany(mappedBy = "user")
//    private  List<MedicalRecord> medicalRecords;


    @ManyToOne
    @JoinColumn(name = "student_id")
    private SubClass studentClass;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private School school;

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
}
