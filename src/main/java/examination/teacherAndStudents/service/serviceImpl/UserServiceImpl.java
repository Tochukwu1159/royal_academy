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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@RequiredArgsConstructor
public class    UserServiceImpl implements UserService {

  private  final   UserRepository userRepository;

   private final  EmailService emailService;

    private final  HttpServletRequest httpServletRequest;


   private final JwtUtil jwtUtil;


    private final Cloudinary cloudinary;


    private final  PasswordEncoder passwordEncoder;

    private final  AuthenticationManager authenticationManager;

    private final CustomUserDetailService customUserDetailsService;

    private final  PasswordResetTokenRepository passwordResetTokenRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final  WalletRepository walletRepository;

    private final  SubClassRepository subClassRepository;

    private final  ClassCategoryRepository classCategoryRepository;

    private final  StudentRecordRepository studentRecordRepository;

    private final  AcademicYearRepository academicYearRepository;

    private final  ModelMapper modelMapper;


    @Override
    public UserResponse createAccount(UserRequestDto userRequest) throws MessagingException {
      AccountUtils.validateEmailAndPassword(userRequest.getEmail(), userRequest.getPassword(), userRequest.getConfirmPassword());
//        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//        // Get the secure URL of the uploaded image from Cloudinary
//        String imageUrl = (String) uploadResult.get("secure_url");

        AcademicYear academicYear = academicYearRepository.findByYear(userRequest.getYear());

        ClassCategory studentClassLevel = classCategoryRepository.findByIdAndAcademicYear(userRequest.getClassCategoryId(), academicYear);

        SubClass subClass = subClassRepository.findByClassCategory(studentClassLevel);


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
                .studentClass(subClass)
                .phoneNumber(userRequest.getPhoneNumber())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .roles(Roles.STUDENT)
//                .profilePicture(imageUrl)
                .build();
        User savedUser = userRepository.save(newUser);
            subClass.setNumberOfStudents(subClass.getNumberOfStudents() + 1);
//            subClass.setUser(savedUser);
            subClassRepository.save(subClass);
        //create student's wallet
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

        return modelMapper.map(savedUser, UserResponse.class);
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
//                Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
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

        return modelMapper.map(savedUser, UserResponse.class);
    }


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



         return modelMapper.map(user, UserResponse.class);
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

        return modelMapper.map(user, UserResponse.class);
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

        return modelMapper.map(user, UserResponse.class);
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

        return modelMapper.map(optionalUsers.get(), UserResponse.class);
    }

    @Override
    public UserResponse getUser() {
        try {
            String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();

            if ("anonymousUser".equals(loggedInEmail)) {
                throw new ResourceNotFoundException("This user does not exist");
            }

            User user = userRepository.findByEmail(loggedInEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return modelMapper.map(user, UserResponse.class);

        } catch (Exception e) {
           throw new CustomInternalServerException("Internal server error "+e);
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

    @Override
    public UserResponse geenerateIdCard(String uniqueRegistrationNumber) {
        try{
            Optional<User> user = userRepository.findByUniqueRegistrationNumber(uniqueRegistrationNumber);
            if(user.isPresent()){
                return modelMapper.map(user.get(), UserResponse.class);
            }else {
                throw new ResourceNotFoundException("User not found");
            }

        }catch (Exception e){
            throw new RuntimeException("Error generating ID card");

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

    public Page<UserResponse> getAllStudentsFilteredAndPaginated(
            Long classCategoryId,
            Long subClassId,
            Long academicYearId,
            int page,
            int size,
            String sortBy
    ) {
        Optional<AcademicYear> academicYearOptional = academicYearRepository.findById(academicYearId);
        Optional<ClassCategory> studentClassLevelOptional = classCategoryRepository.findById(classCategoryId);
        Optional<SubClass> subClassOptional = subClassRepository.findById(subClassId);


        AcademicYear academicYear = academicYearOptional.orElseThrow(() -> new CustomNotFoundException("Academic year not found"));
        ClassCategory studentClassLevel = studentClassLevelOptional.orElseThrow(() -> new CustomNotFoundException("Student class level not found"));
        SubClass subClass = subClassOptional.orElseThrow(() -> new CustomNotFoundException("Subclass not found"));


        // Create Pageable object for pagination
        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        // Fetch students based on filters
        Page<User> students = userRepository.findAllBySubClassesAndYear(
                 subClass, academicYear, paging);

        return students.map((element) -> modelMapper.map(element, UserResponse.class));
    }
}

