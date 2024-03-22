package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.dto.UserResponse;
import examination.teacherAndStudents.entity.AcademicYear;
import examination.teacherAndStudents.entity.SubClass;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.utils.Roles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

        Boolean existsByEmail(String email);
    Optional<User> findByUniqueRegistrationNumber(String uniqueRegistrationNumber);
    long countByRoles(Roles role);

    long countByStudentGuardianNameIsNotNull();

    Page<User> findAllBySubClassesAndYear( SubClass subClass, AcademicYear year, Pageable pageable);

    Optional<User> findByEmail(String email);
    User findByIdAndRoles(Long studentId, Roles roles);

    Boolean existsByUniqueRegistrationNumber(String studentReg);
    Page<User> findAllByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseOrId(String firstName, String lastName, Long id, Pageable pageable);



    User findByEmailAndRoles(String email, Roles roles);


    Page<User> findAllByRoles(Roles role, Pageable pageable);

}
