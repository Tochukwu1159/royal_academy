package examination.teacherAndStudents.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransportResponse {
    private String routeName;
    private String driverAddress;
    private String vehicleNumber;
    private String driverName;
    private String licenceNumber;
    private String phoneNumber;
    // getters and setters
}