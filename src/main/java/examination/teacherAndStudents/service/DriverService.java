package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.DriverRequest;
import examination.teacherAndStudents.dto.DriverResponse;

import java.util.List;
import java.util.Optional;

public interface DriverService {
    DriverResponse createDriver(DriverRequest driver);
    DriverResponse getDriverById(Long driverId);
    DriverResponse updateDriver(Long driverId, DriverRequest updatedDriver);
    void deleteDriver(Long driverId);

    List<DriverResponse> findAllDrivers();
}
