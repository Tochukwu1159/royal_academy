package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.DriverRequest;
import examination.teacherAndStudents.dto.DriverResponse;
import examination.teacherAndStudents.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/drivers")
public class DriverController {
    @Autowired
    private DriverService driverService;

    // Create a new driver
    @PostMapping
    public ResponseEntity<DriverResponse> createDriver(@RequestBody DriverRequest driver) {
        DriverResponse createdDriver = driverService.createDriver(driver);
        return new ResponseEntity<>(createdDriver, HttpStatus.CREATED);
    }

    // Retrieve a driver by ID
    @GetMapping("/{driverId}")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable Long driverId) {
        DriverResponse driverOptional = driverService.getDriverById(driverId);
        return  new ResponseEntity<>(driverOptional, HttpStatus.OK);
    }

    // Update an existing driver
    @PutMapping("/{driverId}")
    public ResponseEntity<DriverResponse> updateDriver(@PathVariable Long driverId, @RequestBody DriverRequest updatedDriver) {
        DriverResponse updated = driverService.updateDriver(driverId, updatedDriver);
        return updated != null ?
                new ResponseEntity<>(updated, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete a driver by ID
    @DeleteMapping("/{driverId}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long driverId) {
        driverService.deleteDriver(driverId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping
    public ResponseEntity<List<DriverResponse>> findAllDrivers(){
       List<DriverResponse> listOfDrivers =  driverService.findAllDrivers();
       return new ResponseEntity<>(listOfDrivers, HttpStatus.OK);
    }
}
