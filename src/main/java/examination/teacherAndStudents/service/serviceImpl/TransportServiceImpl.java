package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.Security.SecurityConfig;
import examination.teacherAndStudents.dto.DriverResponse;
import examination.teacherAndStudents.dto.EmailDetailsToMultipleEmails;
import examination.teacherAndStudents.dto.TransportRequest;
import examination.teacherAndStudents.dto.TransportResponse;
import examination.teacherAndStudents.entity.BusRoute;
import examination.teacherAndStudents.entity.Transport;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.repository.BusRouteRepository;
import examination.teacherAndStudents.repository.TransportRepository;
import examination.teacherAndStudents.repository.UserRepository;
import examination.teacherAndStudents.service.EmailService;
import examination.teacherAndStudents.service.TransportService;
import examination.teacherAndStudents.utils.Roles;
import examination.teacherAndStudents.objectMapper.TransportMapper;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransportServiceImpl implements TransportService {

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    private TransportMapper transportMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BusRouteRepository busRouteRepository;

    @Override
    public TransportResponse createTransport(TransportRequest transportRequest) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            Optional<User> driver = userRepository.findById(transportRequest.getDriverId());
            String driversName = driver.get().getFirstName() + driver.get().getFirstName();
            if (admin == null) {
                throw new CustomNotFoundException("Please login as an Admin");
            }
            Optional<BusRoute> busRoute = busRouteRepository.findById(transportRequest.getBusRouteId());
            if(busRoute == null){
                throw new CustomNotFoundException("Bus route not found");
            }

            Transport transport = transportMapper.mapToTransport(transportRequest);
            transport.setBusRoute(busRoute.get());
            transport.setDriverName(driversName);
            transportRepository.save(transport);

             return  transportMapper.mapToTransportResponse(transport);
        } catch (Exception e) {
            throw new CustomInternalServerException("Error creating transport: " + e.getMessage());
        }
    }


//    public Transport addStudentsToTransport(Long transportId, List<Long> studentIds) {
//        try {
//            Transport transport = transportRepository.findById(transportId)
//                    .orElseThrow(() -> new CustomNotFoundException("Transport not found with ID: " + transportId));
//
//            List<User> studentsToAdd = new ArrayList<>();
//            for (Long studentId : studentIds) {
//                User student = userRepository.findById(studentId)
//                        .orElseThrow(() -> new CustomInternalServerException("Student not found with ID: " + studentId));
//
//                // Set the transport for the 
//                student.setTransport(transport);
//                studentsToAdd.add(student);
//            }
//
//            // Add new students to the 
//            transport.getStudents().addAll(studentsToAdd);
//
//            // Save the transport, which will cascade the changes to the associated students
//            return transportRepository.save(transport);
//        } catch (Exception e) {
//            throw new CustomInternalServerException("Error adding students to transport: " + e.getMessage());
//        }
//    }
    public TransportResponse addStudentsToTransport1(Long transportId, List<Long> studentIds) {
        try {
            Transport transport = transportRepository.findById(transportId)
                    .orElseThrow(() -> new CustomNotFoundException("Transport not found with ID: " + transportId));

            List<User> students = userRepository.findAllById(studentIds);

            transportMapper.addStudentsToTransport(transport, students);

            transportRepository.save(transport);

            return transportMapper.mapToTransportResponse(transport);
        } catch (Exception e) {
            throw new CustomInternalServerException("Error adding students to transport: " + e.getMessage());
        }
    }

    public void sendEmailToStudents(String busRoute, Set<User> students) throws MessagingException {
        if (students == null || students.isEmpty()) {
            return;
        }

        for (User student : students) {
            Map<String, Object> model = new HashMap<>();
            model.put("name", student.getFirstName() + " " + student.getLastName());
            model.put("busRoute", busRoute);

            // Build the email details for each student
            EmailDetailsToMultipleEmails emailDetailsToMultipleEmails = EmailDetailsToMultipleEmails.builder()
                    .toEmails(Collections.singletonList(student.getEmail())) // Use a singleton list for each student
                    .subject("Bus Breakdown Update")
                    .templateName("email-template-bus-breakdown")
                    .model(model)
                    .build();

            // Send the email for each student
            emailService.sendToMultipleEmails(emailDetailsToMultipleEmails);
        }
    }


    @Override
    public TransportResponse updateTransport(Long transportId, TransportRequest updatedTransport) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            Optional<User> driver = userRepository.findById(updatedTransport.getDriverId());
            String driversName = driver.get().getFirstName() + driver.get().getFirstName();
            if (admin == null) {
                throw new CustomNotFoundException("Please login as an Admin");
            }

            Transport transport = transportRepository.findById(transportId)
                    .orElseThrow(() -> new CustomInternalServerException("Transport not found with ID: " + transportId));

            //fields of existing transport with the values from updatedTransport
            transportMapper.updateTransportFromRequest(transport, updatedTransport);

            // Save the updated transport
            Transport savedTransport = transportRepository.save(transport);
            TransportResponse transportResponse = transportMapper.mapToTransportResponse(savedTransport);
            transportResponse.setDriverName(driversName);
            return  transportResponse;
        } catch (Exception e) {
            throw new CustomInternalServerException("Error updating transport: " + e.getMessage());
        }
    }
    @Override
    public void deleteTransport(Long transportId) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new CustomNotFoundException("Please login as an Admin");
            }
            Transport transport = transportRepository.findById(transportId)
                    .orElseThrow(() -> new CustomInternalServerException("Transport not found with ID: " + transportId));
            transportRepository.delete(transport);
        } catch (Exception e) {
            throw new CustomInternalServerException("Error deleting transport: " + e.getMessage());

        }
    }

    @Override
    public List<TransportResponse> getAllTransports() {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);

            if (admin == null) {
                throw new CustomNotFoundException("Please login as an Admin");
            }
            return transportRepository.findAll().stream().map(transportMapper::mapToTransportResponse).collect(Collectors.toList());
        } catch (CustomNotFoundException e) {
            throw new CustomNotFoundException("Failed fetching all transport " + e.getMessage());

        } catch (Exception e) {
            // Wrap and throw a more general exception
            throw new CustomInternalServerException("Unexpected error fetching all transports " +e.getMessage());
        }
    }
    @Override
    public TransportResponse getTransportById(Long transportId) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);

            if (admin == null) {
                throw new CustomNotFoundException("Please login as an Admin");
            }
            Optional<Transport> transport = transportRepository.findById(transportId);
            return transportMapper.mapToTransportResponse(transport.get());

        } catch (CustomNotFoundException e) {
            throw new CustomNotFoundException("Transport not found with ID: " + transportId);
        } catch (Exception e) {
            throw new CustomInternalServerException("Unexpected error fetching transport by ID "+e.getMessage());
        }
    }





}