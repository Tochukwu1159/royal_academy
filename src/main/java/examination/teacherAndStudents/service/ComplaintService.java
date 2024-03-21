package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.ComplaintDto;
import examination.teacherAndStudents.dto.ReplyComplaintDto;
import examination.teacherAndStudents.entity.Complaint;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ComplaintService {
    List<Complaint> getUserComplaints(Long userId);
    Complaint submitComplaint(ComplaintDto feedback);
    Complaint replyToComplaint(Long feedbackId, ReplyComplaintDto replyDto);
    Page<Complaint> getAllComplaint(int pageNo, int pageSize, String sortBy);
}
