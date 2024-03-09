package examination.teacherAndStudents.service.serviceImpl;


import examination.teacherAndStudents.dto.DriverRequest;
import examination.teacherAndStudents.dto.DriverResponse;
import examination.teacherAndStudents.entity.Driver;
import examination.teacherAndStudents.entity.Transport;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.repository.DriverRepository;
import examination.teacherAndStudents.repository.TransportRepository;
import examination.teacherAndStudents.service.DriverService;
import examination.teacherAndStudents.objectMapper.DriverMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DriverServiceImpl implements DriverService {
    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private TransportRepository transportRepository;

    @Override
    public DriverResponse createDriver(DriverRequest driverRequest) {
        try {
            Optional<Transport> assignedBus = transportRepository.findById(driverRequest.getTransportId());

            if (assignedBus.isPresent()) {
                Driver driver = driverMapper.mapToDriver(driverRequest);
                driver.setTransport(assignedBus.get());
                Driver savedDriver = driverRepository.save(driver);
                return driverMapper.mapToDriverResponse(savedDriver);
            } else {
                throw new CustomNotFoundException("Transport not found with ID: " + driverRequest.getTransportId());
            }
        } catch (Exception e) {
            throw new CustomInternalServerException("Error creating driver: " + e.getMessage());
        }
    }


    @Override
    public DriverResponse getDriverById(Long driverId) {
        try {
            Optional<Driver> driverOptional = driverRepository.findById(driverId);
            return driverMapper.mapToDriverResponse(driverOptional.orElseThrow());
        } catch (NoSuchElementException e) {
            throw new CustomNotFoundException("Driver not found with ID: " + driverId);
        }
    }

    @Override
    public DriverResponse updateDriver(Long driverId, DriverRequest updatedDriver) {
        try {
            Optional<Driver> existingDriverOptional = driverRepository.findById(driverId);

            if (existingDriverOptional.isPresent()) {
                Driver existingDriver = existingDriverOptional.get();
                existingDriver.setName(updatedDriver.getName());
                existingDriver.setAddress(updatedDriver.getAddress());
                existingDriver.setPhoneNumber(updatedDriver.getPhoneNumber());
                existingDriver.setLicenceNumber(updatedDriver.getLicenceNumber());
                Driver updatedDriverEntity = driverRepository.save(existingDriver);
                return driverMapper.mapToDriverResponse(updatedDriverEntity);
            } else {
                throw new CustomNotFoundException("Driver not found with ID: " + driverId);
            }
        } catch (Exception e) {
            throw new CustomInternalServerException("Error updating driver: " + e.getMessage());
        }
    }

    @Override
    public void deleteDriver(Long driverId) {
        try {
            if (driverRepository.existsById(driverId)) {
                driverRepository.deleteById(driverId);
            } else {
                throw new CustomNotFoundException("Driver not found with ID: " + driverId);
            }
        } catch (Exception e) {
            throw new CustomInternalServerException("Error deleting driver: " + e.getMessage());
        }
    }

    public List<DriverResponse> findAllDrivers() {
        try {
            List<Driver> drivers = driverRepository.findAll();
            return drivers.stream()
                    .map(driverMapper::mapToDriverResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error occurred while retrieving drivers: {}", e.getMessage(), e);
            throw new CustomInternalServerException("Error occurred while retrieving drivers");
        }
    }
}


 //   List<MedicationResponse> medicationResponseList = medicationRepository.findAll().stream().map((element) -> modelMapper.map(element, MedicationResponse.class)).collect(Collectors.toList());
