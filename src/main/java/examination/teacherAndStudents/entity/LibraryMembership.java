package examination.teacherAndStudents.entity;

import examination.teacherAndStudents.utils.RoleAssigned;
import examination.teacherAndStudents.utils.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.relation.Role;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "library_Membership ")
public class LibraryMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userRole;
    private String userClass;
    private String userName;

    private String memberId;
    @OneToMany(mappedBy = "student")
    private List<BookBorrowing> borrowing;


}
