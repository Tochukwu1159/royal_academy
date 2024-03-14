package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.AcademicYearRequest;
import examination.teacherAndStudents.dto.AcademicYearResponse;
import org.springframework.data.domain.Page;

public interface AcademicYearService {
    AcademicYearResponse createAcademicYear(AcademicYearRequest request);
    AcademicYearResponse editAcademicYear(Long academicYearId, AcademicYearRequest request);
    Page<AcademicYearResponse> findAllAcademicYears(int pageNo, int pageSize);
    AcademicYearResponse findAcademicYearById(Long academicYearId);
    void deleteAcademicYear(Long academicYearId);
}
