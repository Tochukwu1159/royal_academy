package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.TransportRequest;
import examination.teacherAndStudents.dto.TransportResponse;
import examination.teacherAndStudents.entity.User;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.Set;

public interface TransportService {
    TransportResponse createTransport(TransportRequest transportRequest);

    void sendEmailToStudents(String busRoute, Set<User> students) throws MessagingException;
    TransportResponse updateTransport(Long transportId, TransportRequest updatedTransport);
    void deleteTransport(Long transportId);
    List<TransportResponse> getAllTransports();
    TransportResponse getTransportById(Long transportId);
}