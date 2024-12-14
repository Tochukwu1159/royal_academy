package examination.teacherAndStudents.entity;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "school")
@Entity
@Builder
public class School extends BaseEntity {

    private String schoolName;
    private String schoolAddress;

    private String schoolLogoUrl;

    private String phoneNumber;

    @Column(unique = true)
    private String subscriptionKey;
//    @OneToMany(mappedBy = "school")
//   private List<User> students;

    @ElementCollection
    private List<String> selectedServices; // Store selected services by their names

    private LocalDate subscriptionExpiryDate;


    public Collection<Object> selectedServices() {
        return Collections.singleton(this.selectedServices);
    }
}
