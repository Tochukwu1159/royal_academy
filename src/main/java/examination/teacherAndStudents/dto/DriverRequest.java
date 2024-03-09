package examination.teacherAndStudents.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverRequest {
    private String name;

    private String photo;
    private String address;
    private String phoneNumber;
    private String licenceNumber;

    private long transportId;

}
