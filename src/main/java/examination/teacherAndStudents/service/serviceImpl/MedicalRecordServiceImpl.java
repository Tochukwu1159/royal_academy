package examination.teacherAndStudents.service.serviceImpl;


import examination.teacherAndStudents.dto.MedicalRecordRequest;
import examination.teacherAndStudents.entity.MedicalRecord;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.repository.MedicalRecordRepository;
import examination.teacherAndStudents.repository.UserRepository;
import examination.teacherAndStudents.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private UserRepository userRepository;

    // Other dependencies and methods

    public MedicalRecord addMedicalRecord(Long studentId, MedicalRecordRequest medicalRecordRequest) {
        try {
            Optional<User> student = userRepository.findById(studentId);
            if (student == null) {
                throw new CustomNotFoundException("Student not found");
            }

            MedicalRecord medicalRecord = new MedicalRecord();
            medicalRecord.setUser(student.get());
            medicalRecord.setRecordDate(LocalDateTime.now());
            medicalRecord.setDetails(medicalRecordRequest.getDetails());

            // Add any other fields as needed

            return medicalRecordRepository.save(medicalRecord);
        } catch (Exception e) {
            throw new CustomInternalServerException("Error adding medical record: " + e.getMessage());
        }
    }

    public MedicalRecord updateMedicalRecord(Long recordId, MedicalRecordRequest updatedRecordRequest) {
        try {
            MedicalRecord existingRecord = medicalRecordRepository.findById(recordId)
                    .orElseThrow(() -> new CustomNotFoundException("Medical record not found"));

            // Update fields based on the updatedRecordRequest

            existingRecord.setDetails(updatedRecordRequest.getDetails());
            // Update any other fields as needed

            return medicalRecordRepository.save(existingRecord);
        } catch (Exception e) {
            throw new CustomInternalServerException("Error updating medical record: " + e.getMessage());
        }
    }

    public List<MedicalRecord> getAllMedicalRecordsByStudent(Long studentId) {
        try {
            Optional<User> student = userRepository.findById(studentId);
            if (student == null) {
                throw new CustomNotFoundException("Student not found");
            }

            return medicalRecordRepository.findAllByUser(student);
        } catch (Exception e) {
            throw new CustomInternalServerException("Error retrieving medical records: " + e.getMessage());
        }
    }

    // Add other methods as needed for medical records management

}
