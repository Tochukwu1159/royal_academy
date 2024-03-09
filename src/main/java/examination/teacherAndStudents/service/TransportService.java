package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.TransportRequest;
import examination.teacherAndStudents.entity.Transport;
import examination.teacherAndStudents.entity.TransportResponse;
import examination.teacherAndStudents.entity.User;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.Set;

public interface TransportService {
    TransportResponse createTransport(TransportRequest transportRequest) ;
    Transport addStudentsToTransport(Long transportId, List<Long> studentIds);

    TransportResponse updateTransport(Long transportId, TransportRequest updatedTransport);
    void deleteTransport(Long transportId);
    void sendEmailToStudents(String busRoute, Set<User> students) throws MessagingException;

    List<TransportResponse> getAllTransports();
    TransportResponse getTransportById(Long transportId);
}