package examination.teacherAndStudents.entity;

import examination.teacherAndStudents.utils.AvailabilityStatus;
import examination.teacherAndStudents.utils.DormitoryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dormitory_Rooms")
@Entity
@Builder
public class DormitoryRooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String RoomOrHallName;

    private int numberOfBeds;

    private String description;

    private Double costPerBed;

    @ManyToOne
    @JoinColumn(name = "dormitory_id")
    private Dormitory dormitory;

        @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;
    @OneToMany(mappedBy = "dormitory")
    private List<User> user;
}