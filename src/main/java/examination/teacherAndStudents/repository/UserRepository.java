package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.utils.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    User findByIdAndRoles(Long studentId, Roles roles);

    Boolean existsByUniqueRegistrationNumber(String studentReg);



    User findByEmailAndRoles(String email, Roles roles);
    List<User> findUserByRoles(Roles roles);

//    List<User> findByClassLevelId(Long classId);
}
