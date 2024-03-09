package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.Notification;
import examination.teacherAndStudents.entity.Transaction;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.utils.NotificationStutus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findNotificationByUserOrderByCreatedAtDesc(User user);

//    List<Notification> findNotificationEntitiesByTeacherOrderByCreatedAtDesc(TeacherEntity teacher);
//    Notification findNotificationByTeacherAndStudentOrderByCreatedAtDesc(TeacherEntity teacher, StudentEntity student);
    Notification findNotificationByTransactionOrderByCreatedAtDesc(Transaction transaction);

    List<Notification> findNotificationByUserAndNotificationStutusOrderByCreatedAtDesc(User student, NotificationStutus unread);
}
