package hkmu.wadd.dao;

import hkmu.wadd.model.Poll;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findByCourseId(Long courseId);

    @Query("SELECT p FROM Poll p LEFT JOIN FETCH p.comments LEFT JOIN FETCH p.options WHERE p.id = :pollId")
    Optional<Poll> findByIdWithComments(@Param("pollId") Long pollId);

        @EntityGraph(attributePaths = {"options"})
        @Query("SELECT p FROM Poll p LEFT JOIN FETCH p.options WHERE p.id = :id")
        Optional<Poll> findWithOptionsById(@Param("id") Long id);

        @EntityGraph(attributePaths = {"comments"})
        Optional<Poll> findWithCommentsById(Long id);

}