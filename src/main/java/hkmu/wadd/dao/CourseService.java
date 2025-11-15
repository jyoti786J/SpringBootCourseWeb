package hkmu.wadd.dao;


import hkmu.wadd.exception.CourseNotFoundException;
import hkmu.wadd.model.Course;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private PollRepository pollRepository;

    public Course getCourse() {
        return courseRepository.findAll().iterator().next();
    }


    public Course getCourseView() {
        Course course = getCourse();
        // Initialize collections separately
        Hibernate.initialize(course.getLectures());
        Hibernate.initialize(course.getPolls());
        Hibernate.initialize(course.getComments());
        return course;
    }


    public Course getCourseById(Long courseId) throws CourseNotFoundException {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }

    @PostConstruct
    public Course createPermanentCourse() {
        Course course = new Course();
        course.setCourseName("Online Programmming Course");
        return courseRepository.save(course);
    }

}