package examination.teacherAndStudents.objectMapper;

import examination.teacherAndStudents.dto.DriverRequest;
import examination.teacherAndStudents.dto.DriverResponse;
import examination.teacherAndStudents.entity.Driver;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverMapper {
    private final ModelMapper modelMapper;
    public Driver mapToDriver(DriverRequest driverRequest) {
        return modelMapper.map(driverRequest, Driver.class);
    }

    public DriverResponse mapToDriverResponse(Driver driver) {
        return modelMapper.map(driver, DriverResponse.class);
    }
}
