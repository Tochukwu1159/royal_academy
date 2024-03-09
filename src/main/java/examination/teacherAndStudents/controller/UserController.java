package examination.teacherAndStudents.controller;
import examination.teacherAndStudents.dto.*;
import examination.teacherAndStudents.error_handler.BadRequestException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.service.UserService;
import examination.teacherAndStudents.utils.AccountUtils;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value="/api/v1/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/create")
    public UserResponse createAccount(@RequestBody @Valid UserRequestDto userRequest) throws MessagingException {
        return  userService.createAccount(userRequest);
    }
    @PostMapping("/admin/create")
    public UserResponse createAdmin(@RequestBody @Valid UserRequestDto userRequest) throws MessagingException {
        return  userService.createAdmin(userRequest);
    }

    @PostMapping("/teacher/create")
    public UserResponse createTeacher(@RequestBody @Valid UserRequestDto userRequest) throws MessagingException {
        return  userService.createTeacher(userRequest);
    }


    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody @Valid LoginRequest loginRequest){
        return userService.loginUser(loginRequest);

    }


    @PostMapping("/admin/login")
    public LoginResponse loginAmin(@RequestBody @Valid LoginRequest loginRequest){
        return userService.loginAdmin(loginRequest);

    }

    @PostMapping("/edit")
    public UserResponse editUserDetails( @RequestBody @Valid EditUserRequest editUserDto){
        return userService.editUserDetails(editUserDto);
    }
    @PostMapping("/forgot-password")
    public  UserResponse forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest){
        return userService.forgotPassword(forgotPasswordRequest);
    }

    @GetMapping("/resetPassword")
    public  UserResponse resetPassword(@RequestBody @Valid PasswordResetRequest passwordResetRequest, @RequestParam("token") String token){
        return userService.resetPassword(passwordResetRequest, token);
    }


    @PostMapping("/update")
    public UserResponse updatePassword (@RequestBody @Valid ChangePasswordRequest changePasswordRequest){
        return userService.updatePassword(changePasswordRequest);
    }

//    @DeleteMapping("/delete/{userId}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
//        boolean deleted = userService.deleteUser(userId).hasBody();
//        if (deleted) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }

    @PutMapping("/{studentId}/updateClassLevel")
    public ResponseEntity<UserResponse> updateStudentClassLevel(
            @PathVariable Long studentId,
            @RequestParam Long newSubClassLevelId) {
        try {
            UserResponse response = userService.updateStudentClassLevel(studentId, newSubClassLevelId);
            return ResponseEntity.ok(response);
        } catch (CustomNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserResponse.builder()
                            .responseCode(AccountUtils.ACCOUNT_NOT_FOUND)
                            .responseMessage(e.getMessage())
                            .build());
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest()
                    .body(UserResponse.builder()
                            .responseCode(AccountUtils.ACCOUNT_UPDATE_FAILED)
                            .responseMessage(e.getMessage())
                            .build());
        }
    }
//
//
}
