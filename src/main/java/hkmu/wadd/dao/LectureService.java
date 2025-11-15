package hkmu.wadd.dao;

import hkmu.wadd.controller.LectureController;
import hkmu.wadd.exception.CourseNotFoundException;
import hkmu.wadd.exception.LectureNotFoundException;
import hkmu.wadd.exception.LectureNoteNotFoundException;
import hkmu.wadd.model.Course;
import hkmu.wadd.model.Lecture;
import hkmu.wadd.model.LectureNotes;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class LectureService {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureNotesRepository lectureNotesRepository;

    @Autowired
    private CourseRepository courseRepository;


    public void createLecture(Long courseId, String displayName, LectureController.LectureForm form, String createdBy)
            throws IOException, CourseNotFoundException {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        // Validate lecture number uniqueness
        if (lectureRepository.existsByCourseAndLectureNo(course, form.getLectureNo())) {
            throw new IllegalArgumentException("Lecture number must be unique within a course");
        }


        Lecture lecture = new Lecture();
        lecture.setLectureNo(form.getLectureNo());
        lecture.setLectureName(form.getLectureName());
        lecture.setCourse(course);

        // Process lecture notes
        if (form.getNewLectureNotes() != null) {
            for (MultipartFile file : form.getNewLectureNotes()) {
                if (!file.isEmpty()) {
                    LectureNotes note = new LectureNotes();
                    note.setName(file.getOriginalFilename());
                    note.setName(displayName.trim());
                    note.setMimeContentType(file.getContentType());
                    note.setContents(file.getBytes());
                    note.setUploadedBy(createdBy);
                    note.setUploadedAt(LocalDateTime.now());
                    note.setLecture(lecture);
                    lecture.getLectureNotes().add(note);
                }
            }
        }

        lectureRepository.save(lecture);
    }

    public LectureNotes getLectureNote(Long lectureId, UUID noteId)
            throws LectureNotFoundException, LectureNoteNotFoundException {

        // Verify lecture exists
        if (!lectureRepository.existsById(lectureId)) {
            throw new LectureNotFoundException(lectureId);
        }

        return lectureNotesRepository.findByIdAndLectureId(noteId, lectureId)
                .orElseThrow(() -> new LectureNoteNotFoundException(noteId));
    }

    public void deleteLecture(Long lectureId) throws LectureNotFoundException {
        if (!lectureRepository.existsById(lectureId)) {
            throw new LectureNotFoundException(lectureId);
        }
        lectureRepository.deleteById(lectureId);
    }

    @Transactional
    public void updateLecture(Long courseId, Long lectureId, LectureController.LectureForm form, String updatedBy)
            throws CourseNotFoundException, LectureNotFoundException, IOException {

        // Verify course exists
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException(courseId);
        }

        // Get the lecture with existing notes
        Lecture lecture = lectureRepository.findWithNotesById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException(lectureId));

        // Verify lecture belongs to course
        long course_id = lecture.getCourse().getId();
        if (course_id != courseId) {
            throw new LectureNotFoundException(lectureId);
        }

        // Update basic fields
        lecture.setLectureNo(form.getLectureNo());
        lecture.setLectureName(form.getLectureName());

        // Process new lecture notes
        if (form.getNewLectureNotes() != null) {
            for (MultipartFile file : form.getNewLectureNotes()) {
                if (!file.isEmpty()) {
                    LectureNotes note = new LectureNotes();
                    note.setName(file.getOriginalFilename());
                    note.setMimeContentType(file.getContentType());
                    note.setContents(file.getBytes());
                    note.setUploadedBy(updatedBy);
                    note.setUploadedAt(LocalDateTime.now());
                    note.setLecture(lecture);
                    lecture.getLectureNotes().add(note);
                }
            }
        }

        lectureRepository.save(lecture);
    }

    public void addLectureNote(Lecture lecture) {
        lectureRepository.save(lecture);
    }

    public Lecture getLectureWithNotes(Long courseId, Long lectureId)
            throws CourseNotFoundException, LectureNotFoundException {

        // Verify course exists
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException(courseId);
        }

        // Get lecture with notes initialized using entity graph
        Lecture lecture = lectureRepository.findWithNotesById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException(lectureId));

        // Verify lecture belongs to course
        long course_Id = lecture.getCourse().getId();
        if (course_Id !=courseId) {
            throw new LectureNotFoundException(lectureId);
        }

        return lecture;
    }

    @Transactional
    public void deleteNote(Long lectureId, UUID noteId)
            throws LectureNotFoundException, LectureNoteNotFoundException {

        if (!lectureRepository.existsById(lectureId)) {
            throw new LectureNotFoundException(lectureId);
        }

        LectureNotes note = lectureNotesRepository.findById(noteId)
                .orElseThrow(() -> new LectureNoteNotFoundException(noteId));

        if (!note.getLecture().getId().equals(lectureId)) {
            throw new LectureNoteNotFoundException(noteId);
        }

        lectureNotesRepository.delete(note);

        Lecture lecture = note.getLecture();
       // lecture.setLastModified(LocalDateTime.now());
        lectureRepository.save(lecture);
    }
}