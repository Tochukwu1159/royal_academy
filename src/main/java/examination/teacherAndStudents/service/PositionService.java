package examination.teacherAndStudents.service;

import com.itextpdf.text.DocumentException;
import examination.teacherAndStudents.entity.SubClass;
import examination.teacherAndStudents.utils.StudentTerm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Year;

public interface PositionService {
//    void generateAndSaveRanks();
void updateAllPositionsForAClass(SubClass studentClass, Year year, StudentTerm term);
    void generateResultSummaryPdf(Long studentId, Long classLevelId, Year year, StudentTerm term) throws IOException, DocumentException;
}
