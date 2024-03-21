package examination.teacherAndStudents.dto;

import examination.teacherAndStudents.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class MedicationDto {


        @Column(nullable = false)
        private LocalDate recordDate;

        @Column(columnDefinition = "TEXT")
        private String details;
}
