package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.dto.ScoreDTO;
import examination.teacherAndStudents.dto.ScoreRequesDto;
import examination.teacherAndStudents.dto.ScoreRequest;
import examination.teacherAndStudents.entity.*;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.ResourceNotFoundException;
import examination.teacherAndStudents.repository.*;
import examination.teacherAndStudents.service.ResultService;
import examination.teacherAndStudents.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    private final ScoreRepository scoreRepository;

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    @Lazy
    private final ResultService resultService;
    private final SubClassRepository subClassRepository;
    private final AcademicYearRepository academicYearRepository;
    private final ClassCategoryRepository classCategoryRepository;
//
//    private final  ResultService resultService;

    public void addScore(ScoreRequest scoreRequest) {
        User student = userRepository.findById(scoreRequest.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        AcademicYear academicYear = academicYearRepository.findById(scoreRequest.getAcademicYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Academic year  not found"));
        ClassCategory studentClassLevel = classCategoryRepository.findByIdAndAcademicYear(scoreRequest.getClassLevelId(), academicYear);

        SubClass studentClass = subClassRepository.findByIdAndClassCategory(scoreRequest.getSubClassId(),studentClassLevel);


        Subject subject = subjectRepository.findByIdAndTerm(scoreRequest.getSubjectId(), scoreRequest.getTerm())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));


        // Validate scores (add additional validation as needed)
        if (scoreRequest.getExamScore() < 0 || scoreRequest.getAssessmentScore() < 0) {
            throw new IllegalArgumentException("Error adding score: Invalid exam or assessment score");
        }

        // Check if a score already exists for the student and subject
        Score existingScore = scoreRepository.findByUserAndSubClassIdAndSubjectNameAndYearAndTerm(student, scoreRequest.getSubClassId(), subject.getName(),academicYear.getYear(), scoreRequest.getTerm());

        if (existingScore != null) {
            // Update the existing score
            existingScore.setExamScore(scoreRequest.getExamScore());
            existingScore.setAssessmentScore(scoreRequest.getAssessmentScore());
            existingScore.setTerm(scoreRequest.getTerm());
            existingScore.setSubject(subject);
            scoreRepository.save(existingScore);
        } else {
            // Create a new Score object
            Score score = new Score();
            score.setSubjectName(subject.getName());
            score.setExamScore(scoreRequest.getExamScore());
            score.setSubject(subject);
            score.setAssessmentScore(scoreRequest.getAssessmentScore());
            score.setTerm(scoreRequest.getTerm());
            // Save the score
            scoreRepository.save(score);
        }

        // After saving the score, calculate the result using a separate service method
        resultService.calculateResult(scoreRequest.getSubClassId(), student.getId(), subject.getName(), academicYear.getYear(), scoreRequest.getTerm());
    }


    public List<ScoreDTO> getStudentScoresBySubClass(Long subClassId, ScoreRequesDto scoreRequesDto) {

        AcademicYear academicYear = academicYearRepository.findById(scoreRequesDto.getAcademicYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Academic year  not found"));
        ClassCategory studentClassLevel = classCategoryRepository.findByIdAndAcademicYear(scoreRequesDto.getClassLevelId(), academicYear);

        Subject subject = subjectRepository.findByIdAndTerm(scoreRequesDto.getSubjectId(), scoreRequesDto.getTerm())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        SubClass subClass = subClassRepository.findByIdAndClassCategory(scoreRequesDto.getSubClassId(),studentClassLevel)

        List<User> students = subClass.getStudents();
        List<ScoreDTO> scoresDTO = new ArrayList<>();

        for (User student : students) {
            // Retrieve the scores for the student and subject
            Score score = scoreRepository.findByUserAndSubClassAndSubject(student, subClass, subject);
            int examScore = (score != null) ? score.getExamScore() : 0;
            int assessmentScore = (score != null) ? score.getAssessmentScore() : 0;

            // Create a ScoreDTO object
            ScoreDTO scoreDTO = new ScoreDTO();
            scoreDTO.setUserId(student.getId());
            scoreDTO.setName(student.getFirstName() + " " + student.getLastName());
            scoreDTO.setSubject(subject.getName());
            scoreDTO.setExamScore(examScore);
            scoreDTO.setAssessmentScore(assessmentScore);

            // Add the DTO to the list
            scoresDTO.add(scoreDTO);
        }

        return scoresDTO;
    }




    public List<Score> getScoresByStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new CustomInternalServerException("Student not found"));

        return scoreRepository.findScoreByUser(student);
    }

    // You can add more methods for updating and deleting scores as needed
}
