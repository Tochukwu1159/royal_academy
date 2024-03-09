package examination.teacherAndStudents.service.serviceImpl;
import examination.teacherAndStudents.dto.EmailDetails;
import examination.teacherAndStudents.dto.TeacherRequest;
import examination.teacherAndStudents.dto.TeacherResponse;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.objectMapper.TeacherMapper;
import examination.teacherAndStudents.repository.UserRepository;
import examination.teacherAndStudents.service.EmailService;
import examination.teacherAndStudents.service.TeacherService;
import examination.teacherAndStudents.utils.AccountUtils;
import examination.teacherAndStudents.utils.Roles;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

        import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private UserRepository teacherRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
  @Override
    public TeacherResponse createTeacher(TeacherRequest teacherRequest) throws MessagingException {
     AccountUtils.validateEmailAndPassword(teacherRequest.getEmail(), teacherRequest.getPassword(), teacherRequest.getConfirmPassword());
        User teacher = teacherMapper.mapToTeacher(teacherRequest);
        teacher.setUniqueRegistrationNumber(AccountUtils.generateTeacherId());
        teacher.setRoles(Roles.TEACHER);
        teacher.setIsVerified(true);
        teacher.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));
        User savedTeacher = teacherRepository.save(teacher);
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedTeacher.getEmail())
                .subject("ACCOUNT CREATION")
                .templateName("email-template-teachers")  // Name of your Thymeleaf template
                .model(Map.of("name", savedTeacher.getFirstName() + " " + savedTeacher.getLastName()))
                .build();
        emailService.sendHtmlEmail(emailDetails);
        return teacherMapper.mapToTeacherResponse(savedTeacher);
    }
    @Override
    public TeacherResponse getTeacherById(Long teacherId) {
        Optional<User> teacherOptional = teacherRepository.findById(teacherId);
        return teacherOptional.map(teacherMapper::mapToTeacherResponse).orElse(null);
    }
    @Override
    public List<TeacherResponse> findAllTeachers() {
        List<User> teachers = teacherRepository.findAll();
        return teachers.stream().map(teacherMapper::mapToTeacherResponse).toList();
    }
    @Override
    public TeacherResponse updateTeacher(Long teacherId, TeacherRequest updatedTeacher) {
        Optional<User> existingTeacherOptional = teacherRepository.findById(teacherId);

        if (existingTeacherOptional.isPresent()) {
            User existingTeacher = existingTeacherOptional.get();
            existingTeacher.setFirstName(updatedTeacher.getFirstName());
            existingTeacher.setLastName(updatedTeacher.getLastName());
            existingTeacher.setReligion(updatedTeacher.getReligion());
            existingTeacher.setAddress(updatedTeacher.getAddress());
            existingTeacher.setPhoneNumber(updatedTeacher.getPhoneNumber());
            User updatedTeacherEntity = teacherRepository.save(existingTeacher);
            return teacherMapper.mapToTeacherResponse(updatedTeacherEntity);
        } else {
            // Handle teacher not found
            return null;
        }
    }
    @Override
    public void deleteTeacher(Long teacherId) {
        teacherRepository.deleteById(teacherId);
    }
}
