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
public class Book extends BaseEntity{


    private String title;

    private String author;
    private String rackNo;

    private int quantityAvailable; // Quantity of available copies

}
