package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.SickLeaveDTO;
import examination.teacherAndStudents.dto.UserSummaryResponse;
import examination.teacherAndStudents.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {
    UserSummaryResponse getUserSummary();
    Page<User> getAllStudents(int pageNo, int pageSize, String sortBy);

    Page<User> getAllTeachers (int pageNo, int pageSize, String sortBy);
    Page<SickLeaveDTO> getAllSickLeaves(int pageNo, int pageSize, String sortBy);
    void approveSickLeave(Long sickLeaveId);
    void rejectSickLeave(Long sickLeaveId);
}
