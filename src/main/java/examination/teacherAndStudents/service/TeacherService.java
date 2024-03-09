package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.TeacherRequest;
import examination.teacherAndStudents.dto.TeacherResponse;
import jakarta.mail.MessagingException;

import java.util.List;

public interface TeacherService {
    TeacherResponse createTeacher(TeacherRequest teacherRequest) throws MessagingException;
    TeacherResponse getTeacherById(Long teacherId);
    List<TeacherResponse> findAllTeachers();
    TeacherResponse updateTeacher(Long teacherId, TeacherRequest updatedTeacher);
    void deleteTeacher(Long teacherId);

}
