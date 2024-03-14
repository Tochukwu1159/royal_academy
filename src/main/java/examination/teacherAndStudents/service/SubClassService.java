package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.FormTeacherResponse;
import examination.teacherAndStudents.dto.PromoteStudentRequest;
import examination.teacherAndStudents.dto.SubClassRequest;
import examination.teacherAndStudents.dto.SubClassResponse;

import java.util.List;

public interface SubClassService {
    List<SubClassResponse> getAllSubClasses();
    SubClassResponse getSubClassById(Long id);
    SubClassResponse createSubClass(SubClassRequest subClassRequest);
    SubClassResponse updateSubClass(Long id, SubClassRequest subClassRequest);
    void deleteSubClass(Long id);
    PromoteStudentRequest promoteStudents(PromoteStudentRequest request);
    FormTeacherResponse assignFormTeacher(Long teacherId, Long classCategoryId, Long subClassId);
}
