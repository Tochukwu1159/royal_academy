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
public class TimetableServiceImpl implements TimetableService {
    private static final Logger log = LoggerFactory.getLogger(TimetableServiceImpl.class);

    @Autowired
        private TimetableRepository timetableRepository;

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubjectScheduleRepository subjectScheduleRepository;
    @Autowired
    private SubClassRepository subClassRepository;

    @Transactional
        @Override
    public Timetable createTimetable(Long schoolClassId, DayOfWeek dayOfWeek, List<SubjectScheduleRequest> subjectSchedules, TimetableType timetableType, StudentTerm term, Year year) {
        try {
            // Ensure the user has admin role
            String email = SecurityConfig.getAuthenticatedUserEmail();
            User admin = userRepository.findByEmailAndRoles(email, Roles.ADMIN);
            if (admin == null) {
                throw new CustomNotFoundException("Please login as an Admin");
            }

            // Perform any validation or processing if needed
            Optional<SubClass> classLevel = subClassRepository.findById(schoolClassId);
            if (!classLevel.isPresent()) {
                throw new CustomNotFoundException("Class with ID: " + schoolClassId + " not found");
            }

            // Create a Timetable entity
            Timetable timetable = new Timetable();
            timetable.setSubClass(classLevel.get());
            timetable.setDayOfWeek(dayOfWeek);
            timetable.setTerm(term);
            timetable.setYear(year);
            timetable.setTimetableType(timetableType); // Set the timetable type

            // Create SubjectSchedule entities and associate them with the timetable
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
                schedule.setTeachingStatus(TeachingStatus.NOT_TAUGHT);
                schedule.setEndTime(scheduleRequest.getEndTime());
                schedule.setTimetable(timetable);
                schedules.add(schedule);
            }

            timetable.setSubjectSchedules(schedules);
            // Save the timetable and associated subject schedules
            timetable = timetableRepository.save(timetable);

            return timetable;
        } catch (CustomNotFoundException e) {
            // Log the exception
            log.error("Error creating timetable: " + e.getMessage(), e);
            // Rethrow the custom exception
            throw new CustomNotFoundException("Error creating timetable: " + e.getMessage());
        } catch (Exception e) {
            // Log the exception
            log.error("Unexpected error creating timetable " + e.getMessage());
            // Wrap and throw a more general exception
            throw new CustomInternalServerException("Unexpected error creating timetable " + e.getMessage());
        }
    }



   @Override
    public Timetable updateTimetable(Long timetableId, Long schoolClassId, DayOfWeek dayOfWeek, List<SubjectScheduleRequest> subjectSchedules, StudentTerm term, Year year) {
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

            // Perform any validation or processing if needed
            Optional<SubClass> classLevel = subClassRepository.findById(schoolClassId);
            if (!classLevel.isPresent()) {
                throw new CustomNotFoundException("Class with ID: " + schoolClassId + " not found");
            }

            // Update existingTimetable properties
            existingTimetable.setSubClass(classLevel.get());
            existingTimetable.setDayOfWeek(dayOfWeek);
            existingTimetable.setTerm(term);
            existingTimetable.setYear(year);

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
