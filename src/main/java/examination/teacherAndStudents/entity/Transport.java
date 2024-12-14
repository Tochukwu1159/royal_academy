package examination.teacherAndStudents.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "transport")
public class Transport extends BaseEntity{

    @Column(name = "vehicle_number")
    private String vehicleNumber;

    private String driverName;

    private String driverPhoneNumber;

    private String driverLicence;

//    @OneToMany(mappedBy = "transport", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<User> students;


    @OneToOne
    @JoinColumn(name = "bus_route_id", referencedColumnName = "id")
    private BusRoute busRoute;

    // getters and setters
}
