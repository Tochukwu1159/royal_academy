package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.utils.DormitoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DormitoryResponse {

    private Long id;
    private String dormitoryName;

    private DormitoryType type;

    private String address;

    private Double intake;

    private List<DormitoryRoomResponse> dormitoryRoomsList;

}
