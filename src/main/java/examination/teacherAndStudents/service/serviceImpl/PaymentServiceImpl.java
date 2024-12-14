package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.Security.SecurityConfig;
import examination.teacherAndStudents.dto.EmailDetails;
import examination.teacherAndStudents.entity.*;
import examination.teacherAndStudents.error_handler.*;
import examination.teacherAndStudents.repository.*;
import examination.teacherAndStudents.service.EmailService;
import examination.teacherAndStudents.service.PaymentService;
import examination.teacherAndStudents.utils.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.rmi.AlreadyBoundException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {


    private final UserRepository userRepository;


    private final EmailService emailService;



    private final WalletRepository walletRepository;


    private final DueRepository dueRepository;

    private final TransactionRepository transactionRepository;

    private final NotificationRepository notificationRepository;

    private final DuesRepository duesRepository;
    private final ProfileRepository profileRepository;


    @Transactional
    public void payDue(Long dueId) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User user = userRepository.findByEmailAndRoles(email, Roles.STUDENT);
            if (user == null) {
                throw new CustomNotFoundException("Please login as a Student"); // Return unauthorized response for non-admin users
            }
            Optional<Profile> studentProfile = profileRepository.findByUser(user);
            if (studentProfile == null) {
                throw new CustomNotFoundException("Student profile does not exist"); // Return unauthorized response for non-admin users
            }
            Wallet wallet = studentProfile.get().getWallet();
            Dues dues = dueRepository.findById(dueId)
                    .orElseThrow(() -> new EntityNotFoundException("Due not found with id: " + dueId));

            if(dues.getUser().getId() != user.getId()){
                throw new CustomNotFoundException("Due does not belong to the user with id: " + user.getId());
            }
            BigDecimal amountToPay = dues.getAmount();
            if(dues.getStatus() == DuesStatus.SUCCESS){
                throw new UserAlreadyExistException("Dues with id: "+ dueId + " " + "for "+ dues.getPurpose() + " already settled");
            }
            // Check if the wallet has sufficient funds
            if (wallet.getBalance().compareTo(amountToPay) < 0) {
                throw new InsufficientBalanceException("Insufficient funds in the wallet to pay the due");
            }

            // Deduct the amount from the wallet
            wallet.setBalance(wallet.getBalance().subtract(amountToPay));
            wallet.setTotalMoneySent(wallet.getTotalMoneySent().add(amountToPay));
            walletRepository.save(wallet);

            // Update the status of the due (if needed)
            dues.setStatus(DuesStatus.SUCCESS);
            dueRepository.save(dues);
            Transaction transaction = Transaction.builder()
                    .transactionType(TransactionType.DEBIT.name())
                    .user(user)
                    .amount(new BigDecimal(String.valueOf(amountToPay)))
                    .description("You have successfully paid " + amountToPay + " for " + dues.getPurpose())
                    .build();
            transactionRepository.save(transaction);

            Notification notification = Notification.builder()
                    .notificationType(NotificationType.DEBIT_NOTIFICATION)
                    .user(user)
                    .notificationStutus(NotificationStutus.UNREAD)
                    .transaction(transaction)
                    .message("You have paid ₦" + " " + amountToPay + " for " + dues.getPurpose())
                    .build();
            notificationRepository.save(notification);

            Map<String, Object> model = new HashMap<>();
            model.put("amount", amountToPay);
            model.put("name", user.getFirstName() + " " + user.getLastName());
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(user.getEmail())
                    .subject("Wallet funding status")
                    .templateName("email-template-wallet")
                    .model(model)
                    .build();
            emailService.sendEmails(emailDetails);

        } catch (EntityNotFoundException e) {
            // Handle entity not found exception
            throw new NotFoundException("User or due not found " + e.getMessage());

        } catch (InsufficientBalanceException e) {
            throw new InsufficientBalanceException( "Insufficient funds to pay the due "+ e.getMessage());

        } catch (DataAccessException e) {
            // Handle database-related exceptions
            throw new examination.teacherAndStudents.error_handler.ResponseStatusException("An error occurred while processing the payment "+ e.getMessage());

        } catch (Exception e) {
            throw new ResponseStatusException("An unexpected error occurred " + e.getMessage());
        }
    }

    public void submitReceiptPhoto(Long duesId, byte[] receiptPhoto) {
        Dues dues = duesRepository.findById(duesId)
                .orElseThrow(() -> new EntityNotFoundException("Dues not found with ID: " + duesId));

        dues.setReceiptPhoto(receiptPhoto);
        dues.setStatus(DuesStatus.PENDING); // Set the status to pending after receipt submission

        duesRepository.save(dues);
    }

    public void reviewAndSetStatus(Long duesId, DuesStatus newStatus) {
        // Step 1: Find the dues record by ID
        Dues dues = duesRepository.findById(duesId)
                .orElseThrow(() -> new EntityNotFoundException("Dues not found with ID: " + duesId));
        if (!adminReviewPassed(dues)) {
            throw new CustomInternalServerException("Admin review failed for Dues ID: " + duesId);
        }
        dues.setStatus(newStatus);
        duesRepository.save(dues);
    }

    private  boolean adminReviewPassed(Dues dues) {
        byte[] receiptPhoto = dues.getReceiptPhoto();
        if (receiptPhoto == null || receiptPhoto.length == 0) {
            return false;
        }

        return true;
    }
}

