package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.Security.SecurityConfig;
import examination.teacherAndStudents.dto.DormitoryRequest;
import examination.teacherAndStudents.dto.DormitoryRoomResponse;
import examination.teacherAndStudents.entity.*;
import examination.teacherAndStudents.error_handler.*;
import examination.teacherAndStudents.repository.*;
import examination.teacherAndStudents.service.DormitoryService;
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

@Service
@RequiredArgsConstructor
public class DormitoryServiceImpl implements DormitoryService {

    private final  DormitoryRepository dormitoryRepository;

    private final UserRepository userRepository;



    @Override
        public Page<Dormitory> getAllDormitorys(int pageNo, int pageSize, String sortBy){
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getRoles() == Roles.ADMIN) {
                    Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
                    // User is an admin, fetch all dormitorys
                    Page<Dormitory> dormitories = dormitoryRepository.findAll(paging);
                    return dormitories;
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
    public Optional<Dormitory> getDormitoryById(Long dormitoryId) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getRoles() == Roles.ADMIN) {
                    return dormitoryRepository.findById(dormitoryId);
                } else {
                    throw new CustomNotFoundException("Please log in as an Admin"); // Return unauthorized response for non-admin users
                }
            } else {
                throw new CustomNotFoundException("User not found"); // Handle the case where the user is not found
            }
        } catch (Exception e) {
            throw new CustomInternalServerException("Error fetching the dormitory: " + e.getMessage());
        }
    }


    @Override
    public Dormitory createDormitory(DormitoryRequest dormitory) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getRoles() == Roles.ADMIN) {
                    Dormitory newDormitory = new Dormitory();
                    newDormitory.setAddress(dormitory.getAddress());
                    newDormitory.setType(dormitory.getType());
                    newDormitory.setIntake(dormitory.getIntake());
                    newDormitory.setDormitoryName(dormitory.getDormitoryName());
                    return dormitoryRepository.save(newDormitory);
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
    public Dormitory updateDormitory(Long dormitoryId, DormitoryRequest updatedDormitory) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                if (user.getRoles() == Roles.ADMIN) {
                    Optional<Dormitory> optionalDormitory = dormitoryRepository.findById(dormitoryId);

                    if (optionalDormitory.isPresent()) {
                        Dormitory dormitory = optionalDormitory.get();
                        dormitory.setDormitoryName(updatedDormitory.getDormitoryName());
                        dormitory.setIntake(updatedDormitory.getIntake());
                        dormitory.setAddress(updatedDormitory.getAddress());
                        dormitory.setType(updatedDormitory.getType());

                        return dormitoryRepository.save(dormitory);
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
    public void deleteDormitory(Long dormitoryId) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                if (user.getRoles() == Roles.ADMIN) {
                    // Retrieve the dormitory from the database
                    Optional<Dormitory> existingDormitory = dormitoryRepository.findById(dormitoryId);

                    if (existingDormitory.isPresent()) {
                        dormitoryRepository.deleteById(dormitoryId);
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


}