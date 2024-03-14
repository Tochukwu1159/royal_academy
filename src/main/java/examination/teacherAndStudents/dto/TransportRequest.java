package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.entity.BusRoute;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
    private Long busRouteId;
    private String vehicleNumber;
    private Long driverId;

    private String driverPhoneNumber;

    private String driverLicence;



//    private List<Long> studentIds; // IDs of students to be associated with the transport
    // getters and setters
}
