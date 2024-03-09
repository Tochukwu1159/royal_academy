package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.dto.ScoreRequest;
import examination.teacherAndStudents.entity.*;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.ResourceNotFoundException;
import examination.teacherAndStudents.repository.*;
import examination.teacherAndStudents.service.ResultService;
import examination.teacherAndStudents.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    @Lazy
    private ResultService resultService;
    @Autowired
    private SubClassRepository subClassRepository;
//    @Autowired
//    private  ResultService resultService;

    public void addScore(ScoreRequest scoreRequest) {
        User student = userRepository.findById(scoreRequest.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        SubClass studentClass = subClassRepository.findById(scoreRequest.getSubClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Student class not found"));

        Subject subject = subjectRepository.findByIdAndTermAndYear(scoreRequest.getSubjectId(), scoreRequest.getTerm(), scoreRequest.getYear())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        //please check again later
//        List<String> classSubjects = student.getStudentClassLevels().stream()
//                .findFirst() // Assuming you want the first element, modify as needed
//                .map(studentClassLevel -> studentClassLevel.getStudentClass().getSubject().stream()
//                        .map(Subject::getName)
//                        .collect(Collectors.toList()))
//                .orElse(Collections.emptyList());

        // Check if the provided subject is in the list of subjects for the class level
//        if (!classSubjects.contains(subject.getName())) {
//            throw new IllegalArgumentException("Error adding score: Student's class does not include the provided subject");
//        }

        // Validate scores (add additional validation as needed)
        if (scoreRequest.getExamScore() < 0 || scoreRequest.getAssessmentScore() < 0) {
            throw new IllegalArgumentException("Error adding score: Invalid exam or assessment score");
        }

        // Check if a score already exists for the student and subject
        Score existingScore = scoreRepository.findByUserAndSubClassIdAndSubjectNameAndYearAndTerm(student, scoreRequest.getSubClassId(), subject.getName(),scoreRequest.getYear(), scoreRequest.getTerm());

        if (existingScore != null) {
            // Update the existing score
            existingScore.setExamScore(scoreRequest.getExamScore());
            existingScore.setAssessmentScore(scoreRequest.getAssessmentScore());
            existingScore.setYear(scoreRequest.getYear());
            existingScore.setTerm(scoreRequest.getTerm());
            existingScore.setSubClass(studentClass);
            scoreRepository.save(existingScore);
        } else {
            // Create a new Score object
            Score score = new Score();
            score.setUser(student);
            score.setSubjectName(subject.getName());
            score.setExamScore(scoreRequest.getExamScore());
            score.setSubClass(studentClass);
            score.setAssessmentScore(scoreRequest.getAssessmentScore());
            score.setYear(scoreRequest.getYear());
            score.setTerm(scoreRequest.getTerm());
            // Save the score
            scoreRepository.save(score);
        }

        // After saving the score, calculate the result using a separate service method
        resultService.calculateResult(scoreRequest.getSubClassId(), student.getId(), subject.getName(), scoreRequest.getYear(), scoreRequest.getTerm());
    }





    public List<Score> getScoresByStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new CustomInternalServerException("Student not found"));

        return scoreRepository.findScoreByUser(student);
    }

    // You can add more methods for updating and deleting scores as needed
}
