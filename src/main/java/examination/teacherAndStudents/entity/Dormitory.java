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
@Table(name = "dormitory")
@Entity
@Builder
public class Dormitory extends BaseEntity {

    private String dormitoryName;

    private DormitoryType type;

    private String address;

    private Double intake;

//    @OneToMany(mappedBy = "dormitory", cascade = CascadeType.ALL)
//    private List<DormitoryRooms> dormitoryRoomsList;


}
