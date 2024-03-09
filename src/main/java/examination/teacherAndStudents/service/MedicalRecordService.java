package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.MedicalRecordRequest;
import examination.teacherAndStudents.entity.MedicalRecord;

import java.util.List;

public interface MedicalRecordService {
    MedicalRecord addMedicalRecord(Long studentId, MedicalRecordRequest medicalRecordRequest);
    MedicalRecord updateMedicalRecord(Long recordId, MedicalRecordRequest updatedRecordRequest);
    List<MedicalRecord> getAllMedicalRecordsByStudent(Long studentId);
}
