package hkmu.wadd.dao;

import hkmu.wadd.model.Course;
import hkmu.wadd.model.Lecture;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
//    @EntityGraph(attributePaths = {"lectures"})
//    @Query("SELECT c FROM Course c WHERE c.id = :id")
//    Optional<Course> findWithLecturesById(@Param("id") Long id);
//
//    @EntityGraph(attributePaths = {"polls"})
//    Optional<Course> findWithPollsById(Long id);
//
//    @EntityGraph(attributePaths = {"comments", "lectures", "polls"})
//    @Query("SELECT c FROM Course c")
//    List<Course> findAllWithCommentsAndLecturesAndPolls();

    @EntityGraph(attributePaths = {"lectures", "polls", "comments"})
    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.comments WHERE c.id = :id")
    Optional<Course> findWithEverythingById(@Param("id") Long id);

}