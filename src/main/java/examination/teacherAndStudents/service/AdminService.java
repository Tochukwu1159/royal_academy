package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.SickLeaveDTO;
import examination.teacherAndStudents.entity.User;

import java.util.List;

public interface AdminService {
    List<User> getAllStudents();
    List<User> getAllTeachers();
    List<SickLeaveDTO> getAllSickLeaves();
    void approveSickLeave(Long sickLeaveId);
    void rejectSickLeave(Long sickLeaveId);
}
