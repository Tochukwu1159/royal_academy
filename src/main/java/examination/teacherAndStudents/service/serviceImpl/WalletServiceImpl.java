package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.Security.SecurityConfig;
import examination.teacherAndStudents.dto.*;
import examination.teacherAndStudents.entity.Notification;
import examination.teacherAndStudents.entity.Transaction;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.entity.Wallet;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.repository.NotificationRepository;
import examination.teacherAndStudents.repository.TransactionRepository;
import examination.teacherAndStudents.repository.UserRepository;
import examination.teacherAndStudents.repository.WalletRepository;
import examination.teacherAndStudents.service.EmailService;
import examination.teacherAndStudents.service.PayStackPaymentService;
import examination.teacherAndStudents.service.WalletService;
import examination.teacherAndStudents.utils.NotificationStutus;
import examination.teacherAndStudents.utils.NotificationType;
import examination.teacherAndStudents.utils.Roles;
import examination.teacherAndStudents.utils.TransactionType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final EmailService emailService;

    private final PayStackPaymentService paymentService;
    //    private final TransactionDao transactionDao;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public WalletResponse getStudentWalletBalance() {
        String email = SecurityConfig.getAuthenticatedUserEmail();
        Optional<User> student = userRepository.findByEmail(email);
        if (student == null) {
            throw new CustomNotFoundException("Student with Id " + student.get().getId() + " is not valid");
        }

        Wallet wallet = walletRepository.findWalletByUser(student.get());
        if (wallet == null) {
            throw new CustomNotFoundException("Wallet not found or phone number missing");
        }


        return new WalletResponse(wallet.getBalance(), wallet.getTotalMoneySent());

    }

    @Override
    public PaymentResponse fundWallet(FundWalletRequest fundWalletRequest) throws Exception {
        try {
            fundWalletRequest.setAmount(String.valueOf(Integer.parseInt(fundWalletRequest.getAmount()) * 100));
            String email = SecurityConfig.getAuthenticatedUserEmail();

            PayStackTransactionRequest payStackTransactionRequest = PayStackTransactionRequest.builder()
                    .email(email)
                    .amount(new BigDecimal(fundWalletRequest.getAmount()))
                    .build();

            PayStackTransactionResponse transactionResponse = paymentService.initTransaction(payStackTransactionRequest);

            if (!transactionResponse.isStatus()) {
                throw new Exception("Payment not authorized");
            }

            User student = userRepository.findByEmailAndRoles(email, Roles.STUDENT);

            if (student == null) {
                throw new CustomNotFoundException("Student with email " + email + " is not valid");
            }

            fundWalletRequest.setAmount(String.valueOf(Integer.parseInt(fundWalletRequest.getAmount()) / 100));
            double amount = Double.parseDouble(fundWalletRequest.getAmount());
            DecimalFormat formatter = new DecimalFormat("#,###.00");

            Wallet wallet = walletRepository.findWalletByUser(student);

            if (wallet == null) {
                createNewWalletAndTransaction(student, fundWalletRequest.getAmount(), transactionResponse, formatter);
                sendWalletFundingEmail(student, fundWalletRequest.getAmount());
                return new PaymentResponse("Success");
            }

            updateExistingWalletAndTransaction(student, fundWalletRequest.getAmount(), transactionResponse, formatter, wallet);
            sendWalletFundingEmail(student, fundWalletRequest.getAmount());

            return new PaymentResponse(transactionResponse.getData().getAuthorization_url());
        } catch (Exception ex) {
            ex.printStackTrace(); // Log the exception for debugging
            throw new Exception("Failure funding wallet");
        }
    }

    private void createNewWalletAndTransaction(User student, String amount, PayStackTransactionResponse transactionResponse, DecimalFormat formatter) {
        Wallet walletDao1 = Wallet.builder()
                .balance(new BigDecimal(amount))
                .user(student)
                .build();
        walletRepository.save(walletDao1);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.CREDIT.name())
                .user(student)
                .amount(new BigDecimal(amount))
                .description(transactionResponse.getMessage())
                .build();
        transactionRepository.save(transaction);

        Notification notification = Notification.builder()
                .notificationType(NotificationType.CREDIT_NOTIFICATION)
                .user(student)
                .notificationStutus(NotificationStutus.UNREAD)
                .transaction(transaction)
                .message("You funded your wallet with ₦" + formatter.format(Double.parseDouble(amount)))
                .build();
        notificationRepository.save(notification);
    }

    private void updateExistingWalletAndTransaction(User student, String amount, PayStackTransactionResponse transactionResponse, DecimalFormat formatter, Wallet wallet) {
        BigDecimal result = wallet.getBalance().add(new BigDecimal(amount));
        wallet.setBalance(result);
        wallet.setUser(student);
        walletRepository.save(wallet);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.CREDIT.name())
                .user(student)
                .amount(new BigDecimal(amount))
                .description(transactionResponse.getMessage())
                .build();
        transactionRepository.save(transaction);

        Notification notification = Notification.builder()
                .notificationType(NotificationType.CREDIT_NOTIFICATION)
                .user(student)
                .notificationStutus(NotificationStutus.UNREAD)
                .transaction(transaction)
                .message("You funded your wallet with ₦" + formatter.format(Double.parseDouble(amount)))
                .build();
        notificationRepository.save(notification);
    }

    private void sendWalletFundingEmail(User student, String amount) {
        Map<String, Object> model = new HashMap<>();
        model.put("amount", amount);
        model.put("name", student.getFirstName() + " " + student.getLastName());
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(student.getEmail())
                .subject("Wallet funding status")
                .templateName("email-template-wallet")
                .model(model)
                .build();
        emailService.sendEmails(emailDetails);
    }


}
