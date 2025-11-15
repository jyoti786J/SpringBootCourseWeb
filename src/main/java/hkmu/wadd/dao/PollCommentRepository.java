package hkmu.wadd.dao;

import hkmu.wadd.model.Poll;
import hkmu.wadd.model.PollComment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PollCommentRepository extends JpaRepository<PollComment, Long> {
    List<PollComment> findByPollIdOrderByCreatedAtDesc(Long pollId);
    @Query("SELECT pc FROM PollComment pc JOIN FETCH pc.poll WHERE pc.author.username = :username ORDER BY pc.createdAt DESC")
    List<PollComment> findByAuthorUsernameOrderByCreatedAtDesc(@Param("username") String username);

    void deleteByPollId(Long pollId);

    void deleteByAuthorUsername(String username);
}