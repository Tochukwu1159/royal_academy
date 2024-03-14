package examination.teacherAndStudents.utils;

import examination.teacherAndStudents.error_handler.BadRequestException;
import examination.teacherAndStudents.error_handler.UserAlreadyExistException;
import examination.teacherAndStudents.error_handler.UserPasswordMismatchException;
import examination.teacherAndStudents.repository.LibraryMemberRepository;
import examination.teacherAndStudents.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountUtils {
    public static final String PAGENO = "0";
    public static final String PAGESIZE = "10";
    public  static  final String ACCOUNT_EXISTS_CODE ="001";
    public  static  final String ACCOUNT_NOT_EXIST_CODE ="003";
    public  static  final String ACCOUNT_EXITS_MESSAGE ="This user already exists";
    public  static  final String ACCOUNT_NOT_EXIT_MESSAGE ="This user does not exist";
    public  static  final String ACCOUNT_CREATION_SUCCESS = "002";
    public  static  final String ACCOUNT_CREATION_MESSAGE = "Account has been created successfully";
    public static  final  String TRANSFER_SUCCESSFUL_CODE = "008";
    public static  final  String TRANSFER_SUCCESSFUL_MESSAGE = "Transfer Successful";
    public static  final  String ACCOUNT_UPDATED_SUCCESS = "010";
    public static  final  String ACCOUNT_UPDATED_MESSAGE = "Account Updated Successfully";
    public static  final  String PASSWORD_FORGOT_MAIL_CODE = "011";

    public static  final  String PASSWORD_FORGOT_MAIL_MESSAGE = "Password reset successful kindly check your email";
    public static  final  String PASSWORD_CHANGED_SUCCESS ="012";
    public static  final  String PASSWORD_CHANGED_MESSAGE = "User password successfully changed";
    public static  final  String UPDATE_PASSWORD_SUCCESS = "013";
    public static  final  String UPDATE_PASSWORD_SUCCESS_MESSAGE="Your password is successfully updated";
    public static  final  String PASSWORD_DO_NOT_MATCH = "014";
    public static  final  String PASSWORD_DO_NOT_MATCH_MESSAGE = "Password does not match confirm password";
    public static  final  String USER_NOT_LOGGED_IN = "015";

    public static  final  String USER_NOT_LOGGED_IN_MESSAGE = "User not logged in";
    public  static  final String EVENT_WITH_USERS_SUCCESS ="016";
    public  static  final String EVENT_WITH_USERS_SUCCESS_MESSAGE ="Event and users fetched successfully";
    public static  final  String FETCH_ALL_USERS_SUCCESSFUL_CODE = "017";
    public static  final  String FETCH_ALL_USERS_SUCCESSFUL_MESSAGE = "All users fetched Successful";
    public static final String INTERNAL_SERVER_ERROR_CODE = "018";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";

    public static final String PAYSTACK_TRANSACTION_INITIALIZER ="https://api.paystack.co/transaction/initialize";
    public static final String ACCOUNT_UPDATE_SUCCESS = "019";
    public static final String ACCOUNT_NOT_FOUND = "020" ;
    public static final String ACCOUNT_UPDATE_FAILED = "021";

    @Contract(value = "_, null -> false", pure = true)
    public static boolean validatePassword(String password, String cpassword) {
        return password.equals(cpassword);
    }


    private static UserRepository userRepository;
    private static LibraryMemberRepository libraryMemberRepository;

    // Constructor to initialize userRepository
    public AccountUtils(UserRepository userRepository) {
        AccountUtils.userRepository = userRepository;
    }

        public static final String generateStudentId() {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            String yearString = String.valueOf(currentYear);
            StringBuilder randomNumbers = new StringBuilder();
            SecureRandom random = new SecureRandom();
            for (int i = 0; i < 4; i++) {
                int randomNumber = random.nextInt(10);
                randomNumbers.append(randomNumber);
            }

            return  "STU" + yearString + randomNumbers;
        }

    public static final String generateTeacherId() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String yearString = String.valueOf(currentYear);

        SecureRandom random = new SecureRandom();
        String teacherId;

        do {
            StringBuilder randomNumbers = new StringBuilder();
            // Generate 3 random numbers
            for (int i = 0; i < 3; i++) {
                int randomNumber = random.nextInt(10);
                randomNumbers.append(randomNumber);
            }

            // Combine "TECH" + current year + 3 random numbers
            teacherId = "TECH" + yearString + randomNumbers;
        } while (userRepository.existsByUniqueRegistrationNumber(teacherId));

        return teacherId;
    }

    public static final String generateAdminId() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String yearString = String.valueOf(currentYear);

        SecureRandom random = new SecureRandom();
        String adminId;

        do {
            StringBuilder randomNumbers = new StringBuilder();
            // Generate 3 random numbers
            for (int i = 0; i < 3; i++) {
                int randomNumber = random.nextInt(10);
                randomNumbers.append(randomNumber);
            }

            // Combine "ADMIN" + current year + 3 random numbers
            adminId = "ADMIN" + yearString + randomNumbers;
        } while (userRepository.existsByUniqueRegistrationNumber(adminId));

        return adminId;
    }

    public static final String generateStaffId() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String yearString = String.valueOf(currentYear);

        SecureRandom random = new SecureRandom();
        String staffId;

        do {
            StringBuilder randomNumbers = new StringBuilder();
            // Generate 3 random numbers
            for (int i = 0; i < 3; i++) {
                int randomNumber = random.nextInt(10);
                randomNumbers.append(randomNumber);
            }

            // Combine "ADMIN" + current year + 3 random numbers
            staffId = "STAFF" + yearString + randomNumbers;
        } while (userRepository.existsByUniqueRegistrationNumber(staffId));

        return staffId;
    }

    public static final String generateLibraryId() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String yearString = String.valueOf(currentYear);

        SecureRandom random = new SecureRandom();
        String libraryId;

        do {
            StringBuilder randomNumbers = new StringBuilder();
            // Generate 3 random numbers
            for (int i = 0; i < 3; i++) {
                int randomNumber = random.nextInt(10);
                randomNumbers.append(randomNumber);
            }

            // Combine "ADMIN" + current year + 3 random numbers
            libraryId = "LIB" + yearString + randomNumbers;
        } while (libraryMemberRepository.existsByMemberId(libraryId));

        return libraryId;
    }


    public static final String localDateTimeConverter(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM , yyyy hh:mm a", Locale.US);
        return formatter.format(localDateTime);
    }

    public static final boolean isValidEmail(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
        if (email == null) {
            throw new BadRequestException("Error: Email cannot be null");
        }
        Matcher m = p.matcher(email);
        return m.matches();
    }
    public static final boolean existsByMail(String email) {
        return userRepository.existsByEmail(email);


    }


    public static final void validateEmailAndPassword(String email, String password, String confirmPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistException("User with email already exists");
        }

        if (!AccountUtils.validatePassword(password, confirmPassword)) {
            throw new UserPasswordMismatchException("Password does not match");
        }

        if (AccountUtils.existsByMail(email)) {
            throw new BadRequestException("Error: Email is already taken!");
        }

        if (!AccountUtils.isValidEmail(email)) {
            throw new BadRequestException("Error: Email must be valid");
        }

        if (password.length() < 8 || confirmPassword.length() < 8) {
            throw new BadRequestException("Password is too short, should be a minimum of 8 characters long");
        }
    }

    }
