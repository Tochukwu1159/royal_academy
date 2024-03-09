package examination.teacherAndStudents.objectMapper;

import examination.teacherAndStudents.dto.TransportRequest;
import examination.teacherAndStudents.entity.Transport;
import examination.teacherAndStudents.entity.TransportResponse;
import examination.teacherAndStudents.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
    @RequiredArgsConstructor
    public class TransportMapper {
        private final ModelMapper modelMapper;

        public Transport mapToTransport(TransportRequest transportRequest) {
            return modelMapper.map(transportRequest, Transport.class);
        }

        public TransportResponse mapToTransportResponse(Transport transport) {
            return modelMapper.map(transport, TransportResponse.class);
        }


        public void updateTransportFromRequest(Transport transport, TransportRequest transportRequest) {
            if (transportRequest.getRouteName() != null) {
                transport.setRouteName(transportRequest.getRouteName());
            }

            if (transportRequest.getVehicleNumber() != null) {
                transport.setVehicleNumber(transportRequest.getVehicleNumber());
            }

            if (transportRequest.getLicenceNumber() != null) {
                transport.setPhoneNumber(transportRequest.getPhoneNumber());
            }
        }

        public void addStudentsToTransport(Transport transport, List<User> students) {
            Set<User> existingStudents = transport.getStudents();
            existingStudents.addAll(students);
            transport.setStudents(existingStudents);
        }
    }

