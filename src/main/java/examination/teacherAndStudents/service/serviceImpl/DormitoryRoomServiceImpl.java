package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.Security.SecurityConfig;
import examination.teacherAndStudents.dto.ClassCategoryResponse;
import examination.teacherAndStudents.dto.DormitoryRequest;
import examination.teacherAndStudents.dto.DormitoryRoomRequest;
import examination.teacherAndStudents.dto.DormitoryRoomResponse;
import examination.teacherAndStudents.entity.*;
import examination.teacherAndStudents.error_handler.*;
import examination.teacherAndStudents.objectMapper.DormitoryRoomsMapper;
import examination.teacherAndStudents.repository.*;
import examination.teacherAndStudents.service.DormitoryRoomService;
import examination.teacherAndStudents.utils.AvailabilityStatus;
import examination.teacherAndStudents.utils.NotificationType;
import examination.teacherAndStudents.utils.Roles;
import examination.teacherAndStudents.utils.TransactionType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DormitoryRoomServiceImpl implements DormitoryRoomService {


    private final DormitoryRoomRepository dormitoryRoomRepository;

    private final UserRepository userRepository;

    private final DormitoryRoomsMapper dormitoryRoomsMapper;

    private  final WalletRepository walletRepository;

    private final TransactionRepository transactionRepository;

    private final NotificationRepository notificationRepository;
    private final DormitoryRepository dormitoryRepository;
    private final ProfileRepository profileRepository;

    @Override
        public Page<DormitoryRoomResponse> getAllDormitoryRooms(int pageNo, int pageSize, String sortBy) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getRoles() == Roles.ADMIN) {
                    // User is an admin, fetch all dormitorys
                    Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
                    Page<DormitoryRooms> dormitories = dormitoryRoomRepository.findAll(paging);
                    return dormitories.map(dormitoryRoomsMapper::mapToDomitoryResponse);
                } else {
                    throw new CustomNotFoundException("Please log in as an Admin"); // Return unauthorized response for non-admin users
                }
            } else {
                throw new CustomNotFoundException("User not found"); // Handle the case where the user is not found
            }
        } catch (Exception e) {
            throw new CustomInternalServerException("Error fetching all dormitorys: " + e.getMessage());
        }
    }

    @Override
    public Optional<DormitoryRoomResponse> getDormitoryRoomById(Long dormitoryId) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getRoles() == Roles.ADMIN) {
                    Optional<DormitoryRooms> dormitoryRoom = dormitoryRoomRepository.findById(dormitoryId);
                    return Optional.ofNullable(dormitoryRoomsMapper.mapToDomitoryResponse(dormitoryRoom.get()));
                } else {
                    throw new CustomNotFoundException("Please log in as an Admin"); // Return unauthorized response for non-admin users
                }
            } else {
                throw new CustomNotFoundException("User not found"); // Handle the case where the user is not found
            }
        } catch (Exception e) {
            throw new CustomInternalServerException("Error fetching all dormitory's: " + e.getMessage());
        }
    }

    public List<DormitoryRoomResponse> getAllAvailableDormitoryRooms() {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getRoles() == Roles.ADMIN) {

                    List<DormitoryRooms> dormitories = dormitoryRoomRepository.findByAvailabilityStatus(AvailabilityStatus.AVAILABLE);
                    return dormitories.stream().map(dormitoryRoomsMapper::mapToDomitoryResponse).collect(Collectors.toList());
                } else {
                    throw new CustomNotFoundException("Please log in as an Admin"); // Return unauthorized response for non-admin users
                }
            } else {
                throw new CustomNotFoundException("User not found"); // Handle the case where the user is not found
            }
        } catch (Exception e) {
            throw new CustomInternalServerException("Error fetching all available dormitorys: " + e.getMessage());
        }
    }


    @Override
    public DormitoryRoomResponse createDormitoryRoom(DormitoryRoomRequest dormitoryRoomRequest) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            Optional<User> optionalUser = userRepository.findByEmail(email);
            Dormitory existingDormitory = dormitoryRepository.findById(dormitoryRoomRequest.getDormitoryId())
                    .orElseThrow(() -> new CustomNotFoundException(" staff not found with ID: " + dormitoryRoomRequest.getDormitoryId()));
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                if (user.getRoles() == Roles.ADMIN) {
                    DormitoryRooms dormitoryRooms = new DormitoryRooms();

                    dormitoryRooms = dormitoryMapper(dormitoryRooms, dormitoryRoomRequest);
                    dormitoryRooms.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
                    dormitoryRooms.setDormitory(existingDormitory);
                     dormitoryRoomRepository.save(dormitoryRooms);
                    return dormitoryRoomsMapper.mapToDomitoryResponse(dormitoryRooms);
                } else {
                    throw new CustomNotFoundException("You do not have permission to create a dormitory. Please log in as an Admin.");
                }
            } else {
                throw new CustomNotFoundException("User not found with email: " + email);
            }
        } catch (Exception e) {
            // Rethrow a more generic exception or handle it based on your application's requirements
            throw new CustomInternalServerException("An error occurred while creating the dormitory. Please try again later.");
        }
    }



    @Override
    public DormitoryRoomResponse updateDormitoryRoom(Long dormitoryId, DormitoryRoomRequest updatedDormitory) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                if (user.getRoles() == Roles.ADMIN) {
                    Optional<DormitoryRooms> optionalDormitory = dormitoryRoomRepository.findById(dormitoryId);

                    if (optionalDormitory.isPresent()) {
                        DormitoryRooms dormitory = optionalDormitory.get();
                        dormitory = dormitoryMapper(dormitory, updatedDormitory);
                        dormitoryRoomRepository.save(dormitory);
                        return dormitoryRoomsMapper.mapToDomitoryResponse(dormitory);
                    } else {
                        throw new CustomNotFoundException("Dormitory not found for ID: " + dormitoryId);
                    }
                } else {
                    throw new CustomNotFoundException("Please log in as an Admin");
                }
            } else {
                throw new CustomNotFoundException("User not found");
            }
        } catch (Exception e) {
            throw new CustomInternalServerException("Error updating dormitory: " + e.getMessage());
        }
    }


    @Override
    public void deleteDormitoryRoom(Long dormitoryId) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                if (user.getRoles() == Roles.ADMIN) {
                    // Retrieve the dormitory from the database
                    Optional<DormitoryRooms> existingDormitory = dormitoryRoomRepository.findById(dormitoryId);

                    if (existingDormitory.isPresent()) {
                        dormitoryRoomRepository.deleteById(dormitoryId);
                    } else {
                        throw new EntityNotFoundException("Dormitory not found with ID: " + dormitoryId);
                    }
                } else {
                    throw new CustomNotFoundException("Please log in as an Admin");
                }
            } else {
                throw new EntityNotFoundException("User not found");
            }
        } catch (Exception e) {
            throw new CustomInternalServerException("Error occurred while deleting the dormitory with ID: " + dormitoryId + ". " + e.getMessage());
        }
    }


    @Override
    public String addStudentToDormitory(Long studentId, Long dormitoryId) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            Optional<User> optionalUser = userRepository.findByEmail(email);
            Optional<Profile> userProfile = profileRepository.findByUser(optionalUser.get());

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                if (user.getRoles() == Roles.ADMIN) {
                    // Retrieve the student from the database
                    User student = userRepository.findById(studentId)
                            .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));
                    // Check if the student is already assigned to a dormitory
                    if (userProfile.get().getDormitory() != null) {
                        throw new CustomNotFoundException("Student already assigned to a dormitory bed");
                    }

                    // Retrieve the dormitory from the database
                    Dormitory existingDormitory = dormitoryRepository.findById(dormitoryId)
                            .orElseThrow(() -> new EntityNotFoundException("Dormitory not found with ID: " + dormitoryId));

                    DormitoryRooms bedToAssign = dormitoryRoomRepository.findByDormitory(existingDormitory);
                    int availableBeds = 0;
                    if (bedToAssign.getAvailabilityStatus() == AvailabilityStatus.AVAILABLE) {
                        availableBeds = bedToAssign.getNumberOfBeds();
                    }
                    if (availableBeds > 0) {
                        userProfile.get().setDormitory(bedToAssign);
                        userRepository.save(student);

                        // Decrease the number of available beds
                        bedToAssign.setNumberOfBeds(availableBeds - 1);

                        // Update the AvailabilityStatus based on the number of available beds
                        if (availableBeds - 1 == 0) {
                            bedToAssign.setAvailabilityStatus(AvailabilityStatus.UNAVAILABLE);
                        } else {
                            bedToAssign.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
                        }
                        // Update the dormitory in the database
                        dormitoryRoomRepository.save(bedToAssign);
                        return "Student assigned successfully to a hostel space";
                    } else {
                        throw new CustomNotFoundException("No available beds in the dormitory");
                    }
                } else {
                    throw new AuthenticationFailedException("Please log in as an Admin");
                }
            } else {
                throw new EntityNotFoundException("User not found with email: " + email);
            }
        } catch (Exception e) {
            throw new CustomNotFoundException("Error adding student to dormitory: " + e.getMessage());
        }
    }

    @Transactional
    public boolean payForDormitory(Long dormitoryId) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            Optional<User> user = userRepository.findByEmail(email);
            if (user == null) {
                throw new CustomNotFoundException("Please login");
            }
            // Fetch user wallet
            Wallet userWallet = walletRepository.findByUserId(user.get().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Wallet not found for user with id: " + user.get().getId()));

            // Fetch dormitory details
            Dormitory dormitory = dormitoryRepository.findById(dormitoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Dormitory not found with id: " + dormitoryId));

            DormitoryRooms dormitoryRooms = dormitoryRoomRepository.findByDormitory(dormitory);

            // Check if there is sufficient balance in the wallet
            BigDecimal costPerBed = new BigDecimal(dormitoryRooms.getCostPerBed());
            if (userWallet.getBalance().compareTo(costPerBed) >= 0 && dormitoryRooms.getNumberOfBeds() > 0) {
                // Deduct the cost from the wallet balance
                BigDecimal newBalance = userWallet.getBalance().subtract(costPerBed);
                userWallet.setBalance(newBalance);
                userWallet.setTotalMoneySent(userWallet.getTotalMoneySent().add(costPerBed));

                // Reduce the number of beds in the dormitory
                int newNumberOfBeds = dormitoryRooms.getNumberOfBeds() - 1;
                dormitoryRooms.setNumberOfBeds(newNumberOfBeds);

                // Update the wallet and dormitory
                walletRepository.save(userWallet);
                dormitoryRoomRepository.save(dormitoryRooms);

                Transaction transaction = Transaction.builder()
                        .transactionType(TransactionType.DEBIT.name())
                        .user(user.get())
                        .amount(new BigDecimal(String.valueOf(costPerBed)))
                        .description("You have successfully paid " + costPerBed + " " + "for dormitory accommodation")
                        .build();
                transactionRepository.save(transaction);

                Notification notification = Notification.builder()
                        .notificationType(NotificationType.DEBIT_NOTIFICATION)
                        .user(user.get())
                        .transaction(transaction)
                        .message("You have paid ₦" + " " + costPerBed + " " + "for dormitory accommodation")
                        .build();
                notificationRepository.save(notification);

                return true; // Payment successful
            } else {
                throw new InsufficientBalanceException("Insufficient funds or no available beds.");
            }
        } catch (EntityNotFoundException e) {

            throw new CustomNotFoundException("Error processing payment. Please try again later.");
        } catch (Exception e) {
            throw new CustomNotFoundException("An unexpected error occurred. Please try again later.");
        }
    }

    private DormitoryRooms dormitoryMapper(DormitoryRooms dormitory, DormitoryRoomRequest updatedDormitory) {
        dormitory.setNumberOfBeds(updatedDormitory.getNumberOfBeds());
        dormitory.setDescription(updatedDormitory.getDescription());
        dormitory.setRoomOrHallName(updatedDormitory.getRoomOrHallName());
        dormitory.setCostPerBed(updatedDormitory.getCostPerBed());
        return dormitory;
    }

}