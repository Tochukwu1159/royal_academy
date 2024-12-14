package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.dto.AcademicYearRequest;
import examination.teacherAndStudents.dto.AcademicYearResponse;
import examination.teacherAndStudents.entity.AcademicYear;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.NotFoundException;
import examination.teacherAndStudents.repository.AcademicYearRepository;
import examination.teacherAndStudents.service.AcademicYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcademicYearServiceImpl implements AcademicYearService {

    private final AcademicYearRepository academicYearRepository;

    public AcademicYearResponse createAcademicYear(AcademicYearRequest request) {

        try {
            AcademicYear academicYear = mapToAcademicYear(request);
            AcademicYear savedAcademicYear = academicYearRepository.save(academicYear);
            return mapToResponse(savedAcademicYear);
        } catch (Exception e) {
            throw new CustomInternalServerException("Error creating academic year " + e);
        }
    }

    public AcademicYearResponse editAcademicYear(Long academicYearId, AcademicYearRequest request) {
        try {
            AcademicYear academicYear = academicYearRepository.findById(academicYearId)
                    .orElseThrow(() -> new NotFoundException("Academic year not found"));
            academicYear.setYear(request.getYear());
            AcademicYear updatedAcademicYear = academicYearRepository.save(academicYear);
            return mapToResponse(updatedAcademicYear);
        } catch (Exception e) {
            throw new CustomInternalServerException("Error editing academic year " +e);
        }
    }

    public Page<AcademicYearResponse> findAllAcademicYears(int pageNo, int pageSize) {
        try {
            Pageable paging = PageRequest.of(pageNo, pageSize);
            Page<AcademicYear> academicYearPage = academicYearRepository.findAll(paging);
            return academicYearPage.map(this::mapToResponse);
        } catch (Exception e) {
            throw new CustomInternalServerException("Error fetching all academic years " +e);
        }
    }

    public AcademicYearResponse findAcademicYearById(Long academicYearId) {
        try {
            AcademicYear academicYear = academicYearRepository.findById(academicYearId)
                    .orElseThrow(() -> new NotFoundException("Academic year not found"));
            return mapToResponse(academicYear);
        } catch (Exception e) {
            throw new CustomInternalServerException("Error finding academic year by ID "+ e);
        }
    }

    public void deleteAcademicYear(Long academicYearId) {
        try {
            academicYearRepository.deleteById(academicYearId);
        } catch (Exception e) {
            throw new CustomInternalServerException("Error deleting academic year "+ e);
        }
    }

    private AcademicYearResponse mapToResponse(AcademicYear academicYear) {
        AcademicYearResponse response = new AcademicYearResponse();
        response.setId(academicYear.getId());
        response.setYear(academicYear.getYear());
        return response;
    }
    private AcademicYear mapToAcademicYear(AcademicYearRequest academicYear) {
        AcademicYear studentAcademicYear = new AcademicYear();
        studentAcademicYear.setAcademicSession(academicYear.getAcademicSession());
        studentAcademicYear.setYear(academicYear.getYear());
        return studentAcademicYear;
    }
}
