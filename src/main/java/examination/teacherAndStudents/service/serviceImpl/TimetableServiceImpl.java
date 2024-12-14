package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.Security.SecurityConfig;
import examination.teacherAndStudents.dto.SubjectScheduleRequest;
import examination.teacherAndStudents.entity.*;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.CustomNotFoundException;
import examination.teacherAndStudents.error_handler.NotFoundException;
import examination.teacherAndStudents.repository.*;
import examination.teacherAndStudents.service.TimetableService;
import examination.teacherAndStudents.utils.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimetableServiceImpl implements TimetableService {
    private static final Logger log = LoggerFactory.getLogger(TimetableServiceImpl.class);

    private final TimetableRepository timetableRepository;

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final SubjectScheduleRepository subjectScheduleRepository;
    private final SubClassRepository subClassRepository;
    private final AcademicYearRepository academicYearRepository;

    @Transactional
    @Override
    public Timetable createTimetable(Long schoolClassId, Long teacherId, DayOfWeek dayOfWeek,
                                     List<SubjectScheduleRequest> subjectSchedules, TimetableType timetableType,
                                     StudentTerm term, Long yearId) {
        try {
            // Ensure the user has admin role
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new CustomNotFoundException("Please login as an Admin");
            }

            // Find the teacher by ID
            User teacher = userRepository.findByIdAndRoles(teacherId, Roles.TEACHER);
            if (teacher == null) {
                throw new CustomNotFoundException("Teacher not found");
            }

            // Find the class level by ID
            Optional<SubClass> classLevelOptional = subClassRepository.findById(schoolClassId);
            SubClass classLevel = classLevelOptional.orElseThrow(() ->
                    new CustomNotFoundException("Class with ID: " + schoolClassId + " not found"));

            // Find the academic year by ID
            Optional<AcademicYear> academicYearOptional = academicYearRepository.findById(yearId);
            AcademicYear academicYear = academicYearOptional.orElseThrow(() ->
                    new CustomNotFoundException("Academic year with ID: " + yearId + " not found"));

            // Create a Timetable entity
            Timetable timetable = new Timetable();
            timetable.setSubClass(classLevel);
            timetable.setDayOfWeek(dayOfWeek);
            timetable.setTerm(term);
            timetable.setYear(academicYear.getYear());
            timetable.setTimetableType(timetableType);
            timetable = timetableRepository.save(timetable);

            // Create SubjectSchedule entities and associate them with the timetable
            for (SubjectScheduleRequest scheduleRequest : subjectSchedules) {
                SubjectSchedule schedule = new SubjectSchedule();

                // Retrieve the Subject by ID
                Optional<Subject> subjectOptional = subjectRepository.findById(scheduleRequest.getSubjectId());
                Subject subject = subjectOptional.orElseThrow(() ->
                        new CustomNotFoundException("Subject not found with ID: " + scheduleRequest.getSubjectId()));

                // Set SubjectSchedule properties
                schedule.setSubject(subject);
                schedule.setStartTime(scheduleRequest.getStartTime());
                schedule.setEndTime(scheduleRequest.getEndTime());
                schedule.setTopic(scheduleRequest.getTopic());
                schedule.setTeachingStatus(TeachingStatus.NOT_TAUGHT);
                schedule.setTimetable(timetable);
                schedule.setTeacher(teacher.getFirstName() + " " + teacher.getLastName()); // Set teacher's full name
                subjectScheduleRepository.save(schedule);
            }


            // Save the timetable and associated subject schedules
            timetable = timetableRepository.save(timetable);

            return timetable;
        } catch (CustomNotFoundException e) {
            log.error("Error creating timetable: " + e.getMessage(), e);
            throw new CustomNotFoundException("Error creating timetable: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error creating timetable: " + e.getMessage(), e);
            throw new CustomInternalServerException("Unexpected error creating timetable: " + e.getMessage());
        }
    }


   @Override
    public Timetable updateTimetable(Long timetableId, Long teacherId, Long schoolClassId, DayOfWeek dayOfWeek, List<SubjectScheduleRequest> subjectSchedules, StudentTerm term, Long yearId) {
        try {
            // Ensure the user has admin role
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new CustomNotFoundException("Please login as an Admin");
            }

            // Retrieve the existing timetable
            Timetable existingTimetable = timetableRepository.findById(timetableId)
                    .orElseThrow(() -> new CustomNotFoundException("Timetable not found with ID: " + timetableId));

            // Retrieve the existing teacher
            User teacher = userRepository.findByIdAndRoles(teacherId, Roles.TEACHER);
            if (teacher == null) {
                throw new CustomNotFoundException("Teacher not found");
            }

            // Perform any validation or processing if needed
            Optional<SubClass> classLevel = subClassRepository.findById(schoolClassId);
            if (!classLevel.isPresent()) {
                throw new CustomNotFoundException("Class with ID: " + schoolClassId + " not found");
            }

            Optional<AcademicYear> academicYear = academicYearRepository.findById(yearId);
            if (!academicYear.isPresent()) {
                throw new CustomNotFoundException("Academic year with ID: " + yearId + " not found");
            }

            // Update existingTimetable properties
            existingTimetable.setSubClass(classLevel.get());
            existingTimetable.setDayOfWeek(dayOfWeek);
            existingTimetable.setTerm(term);
            existingTimetable.setYear(academicYear.get().getYear());

            // Update SubjectSchedule entities and associate them with the timetable
            List<SubjectSchedule> schedules = new ArrayList<>();
            for (SubjectScheduleRequest scheduleRequest : subjectSchedules) {
                SubjectSchedule schedule = new SubjectSchedule();

                // Retrieve the Subject by ID
                Optional<Subject> subjectForSchedule = subjectRepository.findById(scheduleRequest.getSubjectId());
                if (subjectForSchedule.isEmpty()) {
                    throw new CustomNotFoundException("Subject not found with ID: " + scheduleRequest.getSubjectId());
                }


                // Set the Subject for the SubjectSchedule
                schedule.setSubject(subjectForSchedule.get());
                schedule.setStartTime(scheduleRequest.getStartTime());
                schedule.setEndTime(scheduleRequest.getEndTime());
                schedule.setTimetable(existingTimetable);
                schedule.setTeacher(teacher.getFirstName() + teacher.getLastName());
                schedules.add(schedule);
            }

            existingTimetable.setSubjectSchedules(schedules);

            // Save the updated timetable and associated subject schedules
            existingTimetable = timetableRepository.save(existingTimetable);

            return existingTimetable;
        } catch (CustomNotFoundException e) {
            // Log the exception
            log.error("Error updating timetable: " + e.getMessage(), e);
            // Rethrow the custom exception
            throw new CustomNotFoundException("Error updating timetable: " + e.getMessage());
        } catch (Exception e) {
            // Log the exception
            log.error("Unexpected error updating timetable " + e.getMessage());
            // Wrap and throw a more general exception
            throw new CustomInternalServerException("Unexpected error updating timetable " + e.getMessage());
        }
    }


    public List<Timetable> getAllTimetables() {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new CustomNotFoundException("Please login as an Admin");
            }
            return timetableRepository.findAll();
        } catch (Exception e) {
            throw new CustomInternalServerException("Unexpected error fetching all timetable "+e.getMessage());

        }

    }

    public void deleteTimetable(Long timetableId) {
        try {
            // Perform any validation or checks if needed
            Optional<Timetable> existingTimetable = timetableRepository.findById(timetableId);
            if (existingTimetable.isEmpty()) {
                throw new CustomNotFoundException("Timetable not found with ID: " + timetableId);
            }

            // Delete the timetable
            timetableRepository.deleteById(timetableId);

        } catch (CustomNotFoundException e) {
            // Log the exception
            log.error("Error deleting timetable: " + e.getMessage(), e);
            throw new CustomNotFoundException("Error deleting timetable: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error deleting timetable " + e.getMessage());
            throw new CustomInternalServerException("Unexpected error deleting timetable " + e.getMessage());
        }
    }

    public ResponseEntity<Timetable> getTimetableById(Long timetableId) {
        try {
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);

            if (admin == null) {
                throw new CustomNotFoundException("Please login as an Admin");
            }

            Optional<Timetable> timetableOptional = timetableRepository.findById(timetableId);

            if (timetableOptional.isPresent()) {
                return ResponseEntity.ok(timetableOptional.get());
            } else {
                throw new CustomNotFoundException("Timetable not found with ID: " + timetableId);
            }
        } catch (Exception e) {
            throw new CustomInternalServerException("Unexpected error fetching timetable by ID: " + timetableId + ". " + e.getMessage());
        }
    }

    public List<SubjectSchedule> getAllSubjectSchedules() {
        return subjectScheduleRepository.findAll();
    }





    // Other methods related to SubjectSchedule can be added here
}

