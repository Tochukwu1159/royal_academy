package examination.teacherAndStudents.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book")
@Entity
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;
    private String rackNo;

    private int quantityAvailable; // Quantity of available copies
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookBorrowing> bookBorrowings;

    // Constructors, getters, setters, etc.
}
