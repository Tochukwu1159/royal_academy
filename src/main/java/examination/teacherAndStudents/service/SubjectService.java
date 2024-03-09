package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.SubjectRequest;
import examination.teacherAndStudents.dto.SubjectResponse;
import examination.teacherAndStudents.entity.Subject;

import java.util.List;

public interface SubjectService {
    SubjectResponse createSubject(SubjectRequest subject);
    Subject updateSubject(Long subjectId, SubjectRequest updatedSubjectRequest);
    Subject findSubjectById(Long subjectId);
    List<Subject> findAllSubjects();


}
