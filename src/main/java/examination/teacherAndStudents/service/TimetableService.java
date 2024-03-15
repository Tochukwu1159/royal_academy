package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.SubjectScheduleRequest;
import examination.teacherAndStudents.entity.SubjectSchedule;
import examination.teacherAndStudents.entity.Timetable;
import examination.teacherAndStudents.repository.TimetableRepository;
import examination.teacherAndStudents.utils.DayOfWeek;
import examination.teacherAndStudents.utils.StudentTerm;
import examination.teacherAndStudents.utils.TimetableType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Service
public interface TimetableService {

    Timetable createTimetable(Long schoolClassId,Long teacherId, DayOfWeek dayOfWeek, List<SubjectScheduleRequest> subjectSchedules, TimetableType timetableType, StudentTerm term, Long yearId);
    Timetable updateTimetable(Long timetableId, Long schoolClassId, Long teacherId, DayOfWeek dayOfWeek, List<SubjectScheduleRequest> subjectSchedules, StudentTerm term, Long yearId);
    List<Timetable> getAllTimetables();

    ResponseEntity<Timetable> getTimetableById(Long timetableId);
    void deleteTimetable(Long timetableId);}