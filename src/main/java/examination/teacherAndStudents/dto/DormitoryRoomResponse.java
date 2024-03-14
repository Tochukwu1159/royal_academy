package examination.teacherAndStudents.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DormitoryRoomResponse {
    private String RoomOrHallName;

    private String numberOfBeds;

    private String description;

    private Double costPerBed;
}
