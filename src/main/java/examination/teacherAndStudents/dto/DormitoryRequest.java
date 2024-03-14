package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.utils.DormitoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DormitoryRequest {
    private String dormitoryName;

    private DormitoryType type;

    private String address;

    private Double intake;


}
