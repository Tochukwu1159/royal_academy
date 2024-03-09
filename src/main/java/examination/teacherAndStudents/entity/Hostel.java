package examination.teacherAndStudents.entity;

import examination.teacherAndStudents.utils.AvailabilityStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hostel")
@Entity
@Builder
public class Hostel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numberOfBed;
    private  Double costPerBed;

    private String hostelName;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;
    @OneToMany(mappedBy = "hostel")
    private List<User> user;
}
