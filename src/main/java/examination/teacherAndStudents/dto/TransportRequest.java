package examination.teacherAndStudents.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransportRequest {
    private String routeName;
    private String vehicleNumber;
    private String licenceNumber;
    private String phoneNumber;
//    private List<Long> studentIds; // IDs of students to be associated with the transport
    // getters and setters
}
