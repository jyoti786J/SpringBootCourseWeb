package hkmu.wadd.dao;

import hkmu.wadd.dto.CommentTargetType;
import hkmu.wadd.exception.*;
import hkmu.wadd.model.*;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class CommentService {
    private final PollCommentRepository pollCommentRepository;
    private final CourseCommentRepository courseCommentRepository;
    private final LectureCommentRepository lectureCommentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final PollRepository pollRepository;
    private final LectureRepository lectureRepository;

    @Autowired
    public CommentService(PollCommentRepository pollCommentRepository,
                          CourseCommentRepository courseCommentRepository, LectureCommentRepository lectureCommentRepository,
                          CourseRepository courseRepository,
                          UserRepository userRepository, PollRepository pollRepository, LectureRepository lectureRepository) {
        this.pollCommentRepository = pollCommentRepository;
        this.courseCommentRepository = courseCommentRepository;
        this.lectureCommentRepository = lectureCommentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.pollRepository = pollRepository;
        this.lectureRepository = lectureRepository;
    }

    public Course getCourse() throws CourseNotFoundException {
        // Return first course or throw exception if none exists
        return courseRepository.findAll().stream()
                .findFirst()
                .orElseThrow(CourseNotFoundException::new);
    }

    public Course getCourseView() throws CourseNotFoundException {
        Course course = getCourse();
        // Initialize any lazy-loaded collections if needed
        Hibernate.initialize(course.getLectures());
        Hibernate.initialize(course.getPolls());
        return course;
    }


    public void addCourseComment(Long courseId, String content, String username) throws CourseNotFoundException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        User author = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        CourseComment comment = new CourseComment();
        comment.setContent(content);
        comment.setAuthor(author);
        comment.setCourse(course);
        comment.setCreatedAt(LocalDateTime.now());

        courseCommentRepository.save(comment);
    }

    public List<PollComment> getPollComments(Long pollId) {
        return pollCommentRepository.findByPollIdOrderByCreatedAtDesc(pollId);
    }

    public void addPollComment(Long pollId, String content, String authorUsername) throws PollNotFoundException {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new PollNotFoundException(pollId));

        PollComment comment = new PollComment();
        comment.setPoll(poll);
        comment.setContent(content);
        comment.setAuthor(userRepository.findByUsername(authorUsername));
        comment.setCreatedAt(LocalDateTime.now());

        pollCommentRepository.save(comment);
    }

    public LectureComment addLectureComment(Long lectureId, String content, String username) throws LectureNotFoundException {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException(lectureId));
        User author = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        LectureComment comment = new LectureComment();
        comment.setContent(content);
        comment.setLecture(lecture);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());

        return lectureCommentRepository.save(comment);
    }

    public List<LectureComment> getLectureComments(Long lectureId) {
        return lectureCommentRepository.findByLectureIdOrderByCreatedAtDesc(lectureId);
    }


    public void deleteCommentFromHistory(Long commentId, CommentTargetType targetType) {
        switch(targetType) {
            case LECTURE:
                lectureCommentRepository.deleteById(commentId);
                break;
            case COURSE:
                courseCommentRepository.deleteById(commentId);
                break;
            case POLL:
                pollCommentRepository.deleteById(commentId);
                break;
            default:
                throw new IllegalArgumentException("Invalid comment type");
        }
    }

    public void deleteLectureComment(Long commentId, String currentUsername) throws CommentNotFoundException {
        LectureComment comment = lectureCommentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        lectureCommentRepository.delete(comment);
    }

    public void deleteCourseComment(Long commentId, String currentUsername) throws CommentNotFoundException {
        CourseComment comment = courseCommentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        courseCommentRepository.delete(comment);
    }

    public void deletePollComment(Long commentId, String currentUsername) throws CommentNotFoundException {
        PollComment comment = pollCommentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        pollCommentRepository.delete(comment);
    }



}