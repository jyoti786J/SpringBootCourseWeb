package hkmu.wadd.dao;


import hkmu.wadd.model.Course;
import hkmu.wadd.model.Lecture;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    @EntityGraph(attributePaths = {"lectureNotes"})
    @Query("SELECT l FROM Lecture l WHERE l.id = :id")
    Optional<Lecture> findWithNotesById(@Param("id") Long id);

    boolean existsByCourseAndLectureNo(Course course, Integer lectureNo);

}