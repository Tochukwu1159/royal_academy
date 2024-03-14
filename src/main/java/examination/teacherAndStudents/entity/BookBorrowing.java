package examination.teacherAndStudents.entity;

import examination.teacherAndStudents.utils.BorrowingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book_borrowing")
@Entity
@Builder
public class BookBorrowing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private LibraryMembership student; // Assuming User is the entity representing a student

    private LocalDateTime borrowDate;

    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    private BorrowingStatus status; // Enum for tracking if the book is borrowed or returned

    // Constructors, getters, setters, etc.
}
