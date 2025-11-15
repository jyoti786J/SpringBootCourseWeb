package hkmu.wadd.dao;

import hkmu.wadd.model.LectureComment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureCommentRepository extends JpaRepository<LectureComment, Long> {
    List<LectureComment> findByLectureIdOrderByCreatedAtDesc(Long lectureId);
    @Query("SELECT lc FROM LectureComment lc JOIN FETCH lc.lecture WHERE lc.author.username = :username ORDER BY lc.createdAt DESC")
    List<LectureComment> findByAuthorUsernameOrderByCreatedAtDesc(@Param("username") String username);
}