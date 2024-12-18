package examination.teacherAndStudents.repository;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findWalletByUser(User student);

    Optional<Wallet> findByUserId(Long userId);
}
