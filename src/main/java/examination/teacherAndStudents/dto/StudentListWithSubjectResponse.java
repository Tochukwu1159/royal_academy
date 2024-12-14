package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.entity.Subject;
import lombok.Data;

import java.util.List;
@Data
public class StudentListWithSubjectResponse {
    private String studentUniqueUrl;
    private String studentName;
    private List<Subject> subjectList;
}
