package examination.teacherAndStudents.service;

import examination.teacherAndStudents.entity.Result;
import examination.teacherAndStudents.utils.StudentTerm;

import java.time.Year;

public interface ResultService {
    Result calculateResult(Long classLevelId, Long studentId, String subjectName, Year year,StudentTerm term) ;
    void calculateAverageResult(Long userId, Long classLevelId,Year year, StudentTerm term);}