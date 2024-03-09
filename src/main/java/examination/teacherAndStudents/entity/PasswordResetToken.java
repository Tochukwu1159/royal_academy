package examination.teacherAndStudents.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


    @Table(name = "password_reset_tokens")
    @Entity
    @Setter
    @Getter
    public class PasswordResetToken {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        private Long id;
        private String token;
        @OneToOne
        @JoinColumn(name = "users_details_users_id")
        private User usersDetails;

    }

