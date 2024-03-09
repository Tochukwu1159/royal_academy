package examination.teacherAndStudents.repository;

import examination.teacherAndStudents.entity.StudentRecord;
import examination.teacherAndStudents.entity.SubClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Year;
import java.util.Optional;

public interface StudentRecordRepository extends JpaRepository<StudentRecord, Long> {

    Optional<StudentRecord> findBySubClassAndYear(SubClass subClass, Year year);

//    StudentRecord findBySubClassAndYear(String formTeacher, Year year);
}
