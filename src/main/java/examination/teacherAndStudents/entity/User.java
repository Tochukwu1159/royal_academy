package examination.teacherAndStudents.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import examination.teacherAndStudents.utils.ContractType;
import examination.teacherAndStudents.utils.MaritalStatus;
import examination.teacherAndStudents.utils.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
public class User extends BaseEntity{
        @Size(min = 3, message = "Last name can not be less than 3")
        private String firstName;
        @Size(min = 3, message = "Last name can not be less than 3")
    private String lastName;
    private String middleName;
    private Boolean deactivate = false;

    private String email;
    //    @Size(min = 6, message = "Password should have at least 6 characters")
    private String password;
    @Enumerated(value = EnumType.STRING)
    private Roles roles;
    private Boolean isVerified;


}
