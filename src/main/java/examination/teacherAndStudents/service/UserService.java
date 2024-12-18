package examination.teacherAndStudents.service;
import examination.teacherAndStudents.dto.*;
import examination.teacherAndStudents.entity.User;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface UserService {
    UserResponse createAccount(UserRequestDto userRequest) throws MessagingException;
//    UserResponse editStudent(EditUserRequest editUserDto);
    UserResponse createAdmin(UserRequestDto userRequest) throws MessagingException;
     LoginResponse loginAdmin(LoginRequest loginRequest);

    LoginResponse loginUser(LoginRequest loginRequest);

    UserResponse editUserDetails(EditUserRequest editUserDto);

    UserResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    UserResponse resetPassword(PasswordResetRequest passwordRequest, String token);
//    public UserResponse forgotPassword1(ForgotPasswordRequest forgotPasswordRequest);

    UserResponse updatePassword(ChangePasswordRequest changePasswordRequest);

//    AllUserResponse getAllUsers();
    UserResponse getUser();
     ResponseEntity<Void> deleteUser(Long userId);
     UserResponse geenerateIdCard(String uniqueRegistrationNumber);

    User deactivateStudent(String uniqueRegistrationNumber);

    Page<UserResponse> getAllStudentsFilteredAndPaginated(
            Long classCategoryId,
            Long subClassId,
            Long academicYearId,
            int page,
            int size,
            String sortBy
    );



}
