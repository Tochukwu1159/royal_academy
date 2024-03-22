package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.DuesRequest;
import examination.teacherAndStudents.dto.DuesResponse;
import examination.teacherAndStudents.dto.DuesUpdateRequest;
import examination.teacherAndStudents.entity.Dues;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DuesService {
    Page<DuesResponse> getAllDues(int pageNo, int pageSize, String sortBy);
    DuesResponse getDuesById(Long id);
    List<DuesResponse> updateDuesForAllUsers(Long dueId, DuesUpdateRequest duesUpdateRequest);
    DuesResponse createDuesForASudent(Long id, DuesRequest duesRequest);
    List<DuesResponse> createDues(DuesRequest duesRequest);
    DuesResponse updateDuesForAStudent(Long id, DuesRequest updatedDues);
    boolean deleteDues(Long id);
}
