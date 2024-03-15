package examination.teacherAndStudents.service.serviceImpl;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import examination.teacherAndStudents.Security.CustomUserDetailService;
import examination.teacherAndStudents.Security.JwtUtil;
import examination.teacherAndStudents.Security.SecurityConfig;
import examination.teacherAndStudents.dto.*;
import examination.teacherAndStudents.entity.*;
import examination.teacherAndStudents.error_handler.*;
import examination.teacherAndStudents.repository.*;
import examination.teacherAndStudents.service.EmailService;
import examination.teacherAndStudents.service.UserService;
import examination.teacherAndStudents.utils.AccountUtils;
import examination.teacherAndStudents.utils.Roles;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Year;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class    UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    private  HttpServletRequest httpServletRequest;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomUserDetailService customUserDetailsService;
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private SubClassRepository subClassRepository;
    @Autowired
    private StudentClassLevelRepository studentClassLevelRepository;
    @Autowired
    private StudentRecordRepository studentRecordRepository;


    @Override
    public UserResponse createAccount(UserRequestDto userRequest) throws MessagingException {
      AccountUtils.validateEmailAndPassword(userRequest.getEmail(), userRequest.getPassword(), userRequest.getConfirmPassword());
//        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//        // Get the secure URL of the uploaded image from Cloudinary
//        String imageUrl = (String) uploadResult.get("secure_url");

        SubClass studentAssignedClass = subClassRepository.findBySubClassName(userRequest.getClassAssigned());



        User newUser = User.builder()
                .gender(userRequest.getGender())
                .dateOfBirth(userRequest.getDateOfBirth())
                .religion(userRequest.getReligion())
                .admissionDate(userRequest.getAdmissionDate())
                .studentGuardianOccupation(userRequest.getStudentGuardianOccupation())
                .studentGuardianOccupation(userRequest.getStudentGuardianOccupation())
                .studentGuardianName(userRequest.getStudentGuardianName())
                .studentGuardianPhoneNumber(userRequest.getStudentGuardianPhoneNumber())
                .uniqueRegistrationNumber(AccountUtils.generateStudentId())
                .firstName(userRequest.getFirstName())
                .address(userRequest.getAddress())
                .dateOfBirth(userRequest.getDateOfBirth())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .roles(Roles.STUDENT)
                .admissionDate(userRequest.getAdmissionDate())
                .isVerified(true)
                .phoneNumber(userRequest.getPhoneNumber())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .roles(Roles.STUDENT)
//                .profilePicture(imageUrl)
                .build();
        User savedUser = userRepository.save(newUser);

      Optional<StudentRecord>  studentRecord =  studentRecordRepository.findBySubClassAndYear(studentAssignedClass, userRequest.getYear());
        if(studentRecord.isPresent()){
            studentRecord.get().setNumberOfStudents(studentRecord.get().getNumberOfStudents()+1);
            studentRecordRepository.save(studentRecord.get());
        }else {
            StudentRecord newStudentRecord = new StudentRecord();
            newStudentRecord.setSubClass(studentAssignedClass);
            newStudentRecord.setNumberOfStudents(1);
            newStudentRecord.setYear("2025");
            studentRecord = Optional.of(studentRecordRepository.save(newStudentRecord));
        }

        StudentClassLevel newStudentClassLevel = new StudentClassLevel();
//        newStudentClassLevel.setUser(savedUser);
//        newStudentClassLevel.setStudentRecord(studentRecord.get());
        newStudentClassLevel.setStudentClass(studentAssignedClass);
        newStudentClassLevel.setActive(true);
        studentClassLevelRepository.save(newStudentClassLevel);


//        studentAssignedClass.setNumberOfStudents(studentAssignedClass.getNumberOfStudents() + 1);
//        subClassRepository.save(studentAssignedClass);
        //update the number of students in a class
        // Update the number of students in the class
        //create wallet
        Wallet userWallet = new Wallet();
        userWallet.setBalance(BigDecimal.ZERO);
        userWallet.setTotalMoneySent(BigDecimal.ZERO);
        userWallet.setUser(savedUser);
        walletRepository.save(userWallet);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("SUCCESSFULLY REGISTRATION")
                .templateName("email-template")  // Name of your Thymeleaf template
                .model(createModelWithData(userRequest))
//                .model(Map.of("name", savedUser.getFirstName() + " " + savedUser.getLastName()))
                .build();
        emailService.sendHtmlEmail(emailDetails);

        return UserResponse.builder()
                        .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .studentGuardianOccupation(savedUser.getStudentGuardianOccupation())
                        .dateOfBirth(savedUser.getDateOfBirth())
                        .admissionDate(savedUser.getAdmissionDate())
                        .gender(savedUser.getGender())
                        .studentGuardianName(savedUser.getStudentGuardianName())
                        .studentGuardianPhoneNumber(savedUser.getStudentGuardianPhoneNumber())
                        .uniqueRegistrationNumber(savedUser.getUniqueRegistrationNumber())
//                        .classAssigned(savedUser.getSubClasses().toString())
                        .firstName(savedUser.getFirstName())
                        .address(savedUser.getAddress())
                        .phoneNumber(savedUser.getPhoneNumber())
                        .lastName(savedUser.getLastName())
                        .email(savedUser.getEmail())
                        .firstName(savedUser.getFirstName())
                        .lastName(savedUser.getLastName())
                        .email(savedUser.getEmail())
                        .build())

        .build();
    }



    private Map<String, Object> createModelWithData(UserRequestDto user) {
        Map<String, Object> model = new HashMap<>();

        // Add data to the model
        model.put("name", user.getFirstName() + " " + user.getLastName());
        model.put("email", user.getEmail());
        model.put("password", user.getPassword());

        // You can add more data as needed for your email template

        return model;
    }


    @Override
    public UserResponse createAdmin(UserRequestDto userRequest) throws MessagingException {
        AccountUtils.validateEmailAndPassword(userRequest.getEmail(), userRequest.getPassword(), userRequest.getConfirmPassword());
        //        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//        // Get the secure URL of the uploaded image from Cloudinary
//        String imageUrl = (String) uploadResult.get("secure_url");


        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .uniqueRegistrationNumber(AccountUtils.generateAdminId())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .address(userRequest.getAddress())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .isVerified(true)
                .roles(Roles.ADMIN)
                .build();
        User savedUser = userRepository.save(newUser);


        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .templateName("email-template-admin")  // Name of your Thymeleaf template
                .model(Map.of("name", savedUser.getFirstName() + " " + savedUser.getLastName()))
                .build();
        emailService.sendHtmlEmail(emailDetails);

        return UserResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .firstName(savedUser.getFirstName())
                        .lastName(savedUser.getLastName())
                        .phoneNumber(savedUser.getPhoneNumber())
                        .address(savedUser.getAddress())
                        .uniqueRegistrationNumber(savedUser.getUniqueRegistrationNumber())
                        .email(savedUser.getEmail())
                        .build())

                .build();
    }


//    @Override
//    public UserResponse createTeacher(UserRequestDto userRequest) throws MessagingException {
//        if (userRepository.existsByEmail(userRequest.getEmail())) {
//
//            throw new UserAlreadyExistException("User with email already exist");
//
//        }
//        if (!AccountUtils.validatePassword(userRequest.getPassword(), userRequest.getConfirmPassword())){
//            throw new UserPasswordMismatchException("Password does not match");
//
//        }
//        if(AccountUtils.existsByMail(userRequest.getEmail())){
//            throw new BadRequestException("Error: Email is already taken!");
//        }
//
//        if(AccountUtils.isValidEmail(userRequest.getEmail())){
//            throw new BadRequestException("Error: Email must be valid");
//        }
//
//        if(userRequest.getPassword().length() < 8 || userRequest.getConfirmPassword().length() < 8 ){
//            throw new BadRequestException("Password is too short, should be minimum of 8 character longt");
//        }
//        //        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
////        // Get the secure URL of the uploaded image from Cloudinary
////        String imageUrl = (String) uploadResult.get("secure_url");
//
//
//        User newUser = User.builder()
//                .firstName(userRequest.getFirstName())
//                .lastName(userRequest.getLastName())
//                .email(userRequest.getEmail())
//                .formTeacher(userRequest.getFormTeacher())
//                .phoneNumber(userRequest.getPhoneNumber())
//                .password(passwordEncoder.encode(userRequest.getPassword()))
//                .isVerified(true)
//                .uniqueRegistrationNumber(AccountUtils.generateTeacherId())
//                .address(userRequest.getAddress())
//                .gender(userRequest.getGender())
//                .age(userRequest.getAge())
//                .year(Year.now())
//                .religion(userRequest.getReligion())
//                .academicQualification(userRequest.getAcademicQualification())
//                .subjectAssigned(userRequest.getSubjectAssigned())
//                .roles(Roles.TEACHER)
//                .build();
//        User savedUser = userRepository.save(newUser);
//
//
//        //update the form teacher of the class
////        SubClass classAssigned = subClassRepository.findBySubClassName(savedUser.get);
////        classAssigned.setFormTeacher(savedUser.getFirstName() + " " + savedUser.getLastName());
////        subClassRepository.save(classAssigned);
//
//        //update the student record
////        StudentRecord studentRecord = studentRecordRepository.findBySubClassAndYear(savedUser.getFormTeacher() , savedUser.getYear());
////        studentRecord.setFormTeacher(savedUser.getFirstName() + " " + savedUser.getLastName());
////        studentRecordRepository.save(studentRecord);
//
//        EmailDetails emailDetails = EmailDetails.builder()
//                .recipient(savedUser.getEmail())
//                .subject("ACCOUNT CREATION")
//                .templateName("email-template-teachers")  // Name of your Thymeleaf template
//                .model(Map.of("name", savedUser.getFirstName() + " " + savedUser.getLastName()))
//                .build();
//        emailService.sendHtmlEmail(emailDetails);
//
//        return UserResponse.builder()
//                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
//                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
//                .accountInfo(AccountInfo.builder()
//                        .gender(savedUser.getGender())
//                        .uniqueRegistrationNumber(savedUser.getUniqueRegistrationNumber())
//                        .academicQualification(savedUser.getAcademicQualification())
//                        .subjectAssigned(savedUser.getSubjectAssigned())
//                        .firstName(savedUser.getFirstName())
//                        .address(savedUser.getAddress())
//                        .phoneNumber(savedUser.getPhoneNumber())
//                        .lastName(savedUser.getLastName())
//                        .email(savedUser.getEmail())
//                        .firstName(savedUser.getFirstName())
//                        .lastName(savedUser.getLastName())
//                        .email(savedUser.getEmail())
//                        .build())
//
//                .build();
//    }


    public LoginResponse loginUser(LoginRequest loginRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            if (!authenticate.isAuthenticated()) {
                throw new UserPasswordMismatchException("Wrong email or password");
            }

//            UserDetails userDetails = loadUserByUsername(loginRequest.getEmail());
            Optional<User> userDetails = userRepository.findByEmail(loginRequest.getEmail());

            SecurityContextHolder.getContext().setAuthentication(authenticate);
            String token = "Bearer " + jwtUtil.generateToken(loginRequest.getEmail());

            // Create a UserDto object containing user details
            UserDto userDto = new UserDto();
            userDto.setAddress(userDetails.get().getAddress());
            userDto.setAge(userDto.getAge());
            userDto.setGender(userDetails.get().getGender());
            userDto.setPhoneNumber(userDetails.get().getPhoneNumber());
            userDto.setUniqueRegistrationNumber(userDetails.get().getUniqueRegistrationNumber());
            userDto.setFirstName(userDetails.get().getFirstName());
            userDto.setLastName(userDetails.get().getLastName());
            userDto.setEmail(userDetails.get().getEmail());

            return new LoginResponse(token, userDto);
        } catch (BadCredentialsException e) {
            // Handle the "Bad credentials" error here
            throw new AuthenticationFailedException("Wrong email or password");
        }
    }

    public LoginResponse loginTeacher(LoginRequest loginRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            if (!authenticate.isAuthenticated()) {
                throw new UserPasswordMismatchException("Wrong email or password");
            }

//            UserDetails userDetails = loadUserByUsername(loginRequest.getEmail());
            Optional<User> userDetails = userRepository.findByEmail(loginRequest.getEmail());

            SecurityContextHolder.getContext().setAuthentication(authenticate);
            String token = "Bearer " + jwtUtil.generateToken(loginRequest.getEmail());

            // Create a UserDto object containing user details
            UserDto userDto = new UserDto();
            userDto.setAddress(userDetails.get().getAddress());
            userDto.setAge(userDto.getAge());
            userDto.setGender(userDetails.get().getGender());
            userDto.setSubjectAssigned(userDetails.get().getSubjectAssigned());
            userDto.setPhoneNumber(userDetails.get().getPhoneNumber());
            userDto.setAcademicQualification(userDetails.get().getAcademicQualification());
            userDto.setUniqueRegistrationNumber(userDetails.get().getUniqueRegistrationNumber());
            userDto.setFirstName(userDetails.get().getFirstName());
            userDto.setLastName(userDetails.get().getLastName());
            userDto.setEmail(userDetails.get().getEmail());
            return new LoginResponse(token, userDto);
        } catch (BadCredentialsException e) {
            // Handle the "Bad credentials" error here
            throw new AuthenticationFailedException("Wrong email or password");
        }
    }

    public LoginResponse loginAdmin(LoginRequest loginRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            if (!authenticate.isAuthenticated()) {
                throw new UserPasswordMismatchException("Wrong email or password");
            }

//            UserDetails userDetails = loadUserByUsername(loginRequest.getEmail());
            Optional<User> userDetails = userRepository.findByEmail(loginRequest.getEmail());

            SecurityContextHolder.getContext().setAuthentication(authenticate);
            String token = "Bearer " + jwtUtil.generateToken(loginRequest.getEmail());

            // Create a UserDto object containing user details
            UserDto userDto = new UserDto();
            userDto.setAddress(userDetails.get().getAddress());
            userDto.setAge(userDto.getAge());
            userDto.setGender(userDetails.get().getGender());
            userDto.setPhoneNumber(userDetails.get().getPhoneNumber());
            userDto.setFirstName(userDetails.get().getFirstName());
            userDto.setLastName(userDetails.get().getLastName());
            userDto.setEmail(userDetails.get().getEmail());
            return new LoginResponse(token, userDto);
        } catch (BadCredentialsException e) {
            // Handle the "Bad credentials" error here
            throw new AuthenticationFailedException("Wrong email or password");
        }
    }

    @Transactional
    public UserResponse updateStudentClassLevel(Long studentId, Long newSubClassLevelId) {
        User existingUser = userRepository.findById(studentId)
                .orElseThrow(() -> new CustomNotFoundException("User not found with ID: " + studentId));

        Optional<SubClass> newSubClass = subClassRepository.findById(newSubClassLevelId);

        StudentClassLevel existingStudentActiveClassLevel = studentClassLevelRepository.findByUserAndActiveIsTrue(existingUser);
        existingStudentActiveClassLevel.setActive(false);
        studentClassLevelRepository.save(existingStudentActiveClassLevel);

        if (newSubClass.isEmpty()) {
            throw new BadRequestException("Error: Class level not found for class " + newSubClass.getClass());
        }
        StudentClassLevel updatedStudentClass = new StudentClassLevel();
        updatedStudentClass.setStudentClass(newSubClass.get());
//        updatedStudentClass.setUser(existingUser);
        updatedStudentClass.setActive(true);
//        updatedStudentClass.setStudentRecord();
        studentClassLevelRepository.save(updatedStudentClass);



        Optional<StudentRecord>  studentRecord =  studentRecordRepository.findBySubClassAndYear(newSubClass.get(), Year.now());
        if(studentRecord.isPresent()){
            studentRecord.get().setNumberOfStudents(studentRecord.get().getNumberOfStudents()+1);
            studentRecordRepository.save(studentRecord.get());
        }else {
            StudentRecord newStudentRecord = new StudentRecord();
            newStudentRecord.setSubClass(newSubClass.get());
            newStudentRecord.setNumberOfStudents(1);
            newStudentRecord.setYear("2027");
            studentRecord = Optional.of(studentRecordRepository.save(newStudentRecord));
        }


        return UserResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_UPDATE_SUCCESS)
                .responseMessage("Student class level updated successfully")
                .accountInfo(AccountInfo.builder()
                        .uniqueRegistrationNumber(existingUser.getUniqueRegistrationNumber())
//                        .classAssigned(studentNewClass.getClassName())
                        .build())
                .build();
    }



    @Override
    public UserResponse editUserDetails(EditUserRequest editUserDto) {
        String email = SecurityConfig.getAuthenticatedUserEmail();   //why using this method and not autowired
        System.out.println(email+ "email");
        Object loggedInUsername1 = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(loggedInUsername1 + "loggedInUsername1");

        User user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found"));

        if (user == null){
            throw  new CustomNotFoundException( "User does not exist");
        }
        user.setFirstName(editUserDto.getFirstName());
        user.setLastName(editUserDto.getLastName());
        user.setPhoneNumber(editUserDto.getPhoneNumber());
          User updatedUser = userRepository.save(user);



         return UserResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_UPDATED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_UPDATED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .firstName(updatedUser.getFirstName())
                        .lastName(updatedUser.getLastName())
                        .build())

                .build();
    }

    @Override
    public UserResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {

        Optional<User> userOptional = userRepository.findByEmail(forgotPasswordRequest.getEmail());

        if (!userOptional.isPresent()) {
            throw new CustomNotFoundException("User with provided Email not found");
        }

        User user = userOptional.get();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(forgotPasswordRequest.getEmail());

        // Generate a new token
        String token = new JwtUtil().generateToken(user.getEmail());

        // Check if the user already has a PasswordResetToken
        PasswordResetToken existingToken = passwordResetTokenRepository.findByUsersDetails(user);

        if (existingToken != null) {
            // Update the existing token
            existingToken.setToken(token);
//            existingToken.setExpirationDate(new Date()); // Update expiration date if needed
        } else {
            // Create a new PasswordResetToken if none exists
            PasswordResetToken passwordResetTokenEntity = new PasswordResetToken();
            passwordResetTokenEntity.setToken(token);
            passwordResetTokenEntity.setUsersDetails(user);
//            passwordResetTokenRepository.save(passwordResetTokenEntity);
        }


        Map<String, Object> model = new HashMap<>();
        model.put("passwordResetLink", "http://localhost:8080/api/users/resetPassword?token=" + token);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(forgotPasswordRequest.getEmail())
                .subject("PASSWORD RESET LINK")
                .templateName("password-reset-email")  // Thymeleaf template name
                .model(model)  // Pass the model
                .build();

        try {
            emailService.sendEmailWithThymeleaf(emailDetails);  // Use a new method for Thymeleaf email sending
        } catch (Exception e) {
            // Handle the email sending error here
            throw new EmailSendingException("Failed to send the password reset email");
        }

        return UserResponse.builder()
                .responseCode(AccountUtils.PASSWORD_FORGOT_MAIL_CODE)
                .responseMessage(AccountUtils.PASSWORD_FORGOT_MAIL_MESSAGE)
                .accountInfo(null)
                .build();
    }



    @Override
    @Transactional
    public UserResponse resetPassword(PasswordResetRequest passwordRequest, String token) {
        if (!passwordRequest.getNewPassword().equals(passwordRequest.getConfirmPassword())) {
            throw new CustomNotFoundException("Password do not match");
        }

        String email = jwtUtil.extractUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        if (passwordRequest.getNewPassword().length() < 8 || passwordRequest.getConfirmPassword().length() < 8) {
            throw new BadRequestException("Error: Password is too short");
        }

        // Check if the token has expired
//        Date tokenExpirationDate = jwtUtil. extractExpiration(token);
//        Date currentDate = new Date();

//        if (tokenExpirationDate != null && currentDate.after(tokenExpirationDate)) {
            if(jwtUtil.isTokenExpired(token)){
            throw new TokenExpiredException("Token has expired");
        }

        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        userRepository.save(user);

        passwordResetTokenRepository.deleteByToken(token);

        return UserResponse.builder()
                .responseCode(AccountUtils.PASSWORD_CHANGED_SUCCESS)
                .responseMessage(AccountUtils.PASSWORD_CHANGED_MESSAGE)
                .accountInfo(null)
                .build();
    }


    @Override
    public UserResponse updatePassword (ChangePasswordRequest changePasswordRequest) {
        String email = SecurityConfig.getAuthenticatedUserEmail(); // please confirm
        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();
        String confirmPassword = changePasswordRequest.getConfirmPassword();

        Optional<User> optionalUsers = userRepository.findByEmail(email);

        if(optionalUsers.isPresent()){
            User users = optionalUsers.get();
            String encodedPassword = users.getPassword();
            boolean isPasswordAMatch = passwordEncoder.matches(oldPassword, encodedPassword);

            if(!isPasswordAMatch) {
                throw  new BadRequestException("Old Password does not match");
            }

            if(changePasswordRequest.getNewPassword().length() < 8 || changePasswordRequest.getConfirmPassword().length() < 8 ){
                throw new BadRequestException("Error: Password is too short, should be minimum of 8 character long");
            }

            boolean isPasswordEquals = newPassword.equals(confirmPassword);

            if(!isPasswordEquals){
                throw  new BadRequestException("New Password does not match confirm password");
            }

            users.setPassword(passwordEncoder.encode(newPassword));

            userRepository.save(users);
        }

        return  UserResponse.builder()
                .responseCode(AccountUtils.UPDATE_PASSWORD_SUCCESS)               //does it return ok by default
                .responseMessage(AccountUtils.UPDATE_PASSWORD_SUCCESS_MESSAGE)
                .accountInfo(null)
                .build();
    }

    @Override
    public UserResponse getUser() {
        try {
            String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();

            if ("anonymousUser".equals(loggedInEmail)) {
                return UserResponse.builder()
                        .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                        .responseMessage(AccountUtils.ACCOUNT_NOT_EXIT_MESSAGE)
                        .accountInfo(null)
                        .build();
            }

            User user = userRepository.findByEmail(loggedInEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            AccountInfo accountInfo = AccountInfo.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .uniqueRegistrationNumber(user.getUniqueRegistrationNumber())
                    .studentGuardianName(user.getStudentGuardianName())
                    .dateOfBirth(user.getDateOfBirth())
                    .studentGuardianPhoneNumber(user.getStudentGuardianPhoneNumber())
                    .gender(user.getGender())
                    .subjectAssigned(user.getSubjectAssigned())
                    .address(user.getAddress())
                    .academicQualification(user.getAcademicQualification())
                    .build();

            return UserResponse.builder()
                    .responseCode(AccountUtils.USER_NOT_LOGGED_IN)
                    .responseMessage(AccountUtils.USER_NOT_LOGGED_IN_MESSAGE)
                    .accountInfo(accountInfo)
                    .build();
        } catch (Exception e) {
            // Log the exception for further analysis
            e.printStackTrace();
            return UserResponse.builder()
                    .responseCode(AccountUtils.INTERNAL_SERVER_ERROR_CODE)
                    .responseMessage(AccountUtils.INTERNAL_SERVER_ERROR_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
    }
    public ResponseEntity<Void> deleteUser(Long userId) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);

            if (!admin.getIsVerified()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            if (userRepository.existsById(userId)) {
                userRepository.deleteById(userId);
                return ResponseEntity.noContent().build(); // User deleted successfully
            } else {
                return ResponseEntity.notFound().build(); // User not found
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public User deactivateStudent(String uniqueRegistrationNumber) {
        try {
            Optional<User> studentOptional = userRepository.findByUniqueRegistrationNumber(uniqueRegistrationNumber);
            if (studentOptional.isPresent()) {
                User student = studentOptional.get();
                student.setDeactivate(true);
                return userRepository.save(student);
            } else {
                throw new EntityNotFoundException("Student not found with registration number: " + uniqueRegistrationNumber);
            }
        } catch (Exception e) {
            throw new CustomInternalServerException("Error deactivating student" +e);
        }
    }
}

