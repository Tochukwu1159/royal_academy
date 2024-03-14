package examination.teacherAndStudents.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DormitoryRoomRequest {
    private Long dormitoryId;
    private String RoomOrHallName;

    private int numberOfBeds;

    private String description;

    private Double costPerBed;
}
