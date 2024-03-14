package examination.teacherAndStudents.service;
import examination.teacherAndStudents.dto.*;
import examination.teacherAndStudents.entity.User;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

public interface UserService {
    UserResponse createAccount(UserRequestDto userRequest) throws MessagingException;
//    UserResponse editStudent(EditUserRequest editUserDto);
    UserResponse createAdmin(UserRequestDto userRequest) throws MessagingException;
     LoginResponse loginAdmin(LoginRequest loginRequest);

    LoginResponse loginUser(LoginRequest loginRequest);
    UserResponse updateStudentClassLevel(Long studentId, Long newSubClassLevelId);

    UserResponse editUserDetails(EditUserRequest editUserDto);

    UserResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    UserResponse resetPassword(PasswordResetRequest passwordRequest, String token);
//    public UserResponse forgotPassword1(ForgotPasswordRequest forgotPasswordRequest);

    UserResponse updatePassword(ChangePasswordRequest changePasswordRequest);

//    AllUserResponse getAllUsers();
    UserResponse getUser();
     ResponseEntity<Void> deleteUser(Long userId);

    User deactivateStudent(String uniqueRegistrationNumber);



}
