package examination.teacherAndStudents.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "visitors")
public class Visitors extends BaseEntity{

    @Column(name = "name")
    private String name;
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "purpose")
    private String purpose;
    @Column(name = "signIn")
    private LocalDateTime signIn;

    @Column(name = "signOut")
    private LocalDateTime signOut;

}
