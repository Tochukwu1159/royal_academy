package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.DuesRequest;
import examination.teacherAndStudents.entity.Dues;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DuesService {
    Page<Dues> getAllDues(int pageNo, int pageSize, String sortBy);
    Dues getDuesById(Long id);
    Dues createDues(DuesRequest dues);
    Dues updateDues(Long id, DuesRequest updatedDues);
    boolean deleteDues(Long id);
}
