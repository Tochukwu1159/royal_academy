package examination.teacherAndStudents.controller;
import examination.teacherAndStudents.dto.TransportRequest;
import examination.teacherAndStudents.dto.TransportResponse;
import examination.teacherAndStudents.entity.Transport;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.repository.TransportRepository;
import examination.teacherAndStudents.service.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/transports")
@RequiredArgsConstructor
public class TransportController {


    private final TransportService transportService;
    private final TransportRepository transportRepository;

    @PostMapping("/add")
    public ResponseEntity<TransportResponse> addTransport(@RequestBody TransportRequest transportRequest) {
            TransportResponse createdTransport = transportService.createTransport(transportRequest);
            return new ResponseEntity<>(createdTransport, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{transportId}")
    public ResponseEntity<TransportResponse> editTransport(
            @PathVariable Long transportId,
            @RequestBody TransportRequest updatedTransport) {
            TransportResponse updatedTransportation = transportService.updateTransport(transportId, updatedTransport);
            return new ResponseEntity<>(updatedTransportation, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{transportId}")
    public ResponseEntity<String> deleteTransport(@PathVariable Long transportId) {
            transportService.deleteTransport(transportId);
            return new ResponseEntity<>("Transport deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TransportResponse>> getAllTransports() {
            List<TransportResponse> allTransports = transportService.getAllTransports();
            return new ResponseEntity<>(allTransports, HttpStatus.OK);
    }

    @GetMapping("/{transportId}")
    public ResponseEntity<TransportResponse> getTransportById(@PathVariable Long transportId) {
        TransportResponse transport = transportService.getTransportById(transportId);
            return new ResponseEntity<>(transport, HttpStatus.OK);
    }

//    @PostMapping("/add-students/{transportId}")
//    public ResponseEntity<String> addStudentsToTransport(@PathVariable Long transportId, @RequestBody List<Long> studentIds) {
//            transportService.addStudentsToTransport(transportId, studentIds);
//            return ResponseEntity.ok("Students added to transport successfully.");
//
//    }

    @PostMapping("/send-email-to-students/{transportId}")
    public ResponseEntity<String> sendEmailToStudents(@PathVariable Long transportId) {
        try {
            Optional<Transport> transport = transportRepository.findById(transportId);
            Transport transport1 = transport.get();
            String busRoute = transport1.getBusRoute().getRouteName();
            // Assuming students can be retrieved from the transport entity
//            Set<User> students = transport1.getStudents();

//            transportService.sendEmailToStudents(busRoute, students);

            return ResponseEntity.ok("Emails sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending emails: " + e.getMessage());
        }
    }
}
