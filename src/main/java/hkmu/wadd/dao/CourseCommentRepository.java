// CourseCommentRepository.java
package hkmu.wadd.dao;

import hkmu.wadd.model.CourseComment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseCommentRepository extends JpaRepository<CourseComment, Long> {
    List<CourseComment> findByCourseIdOrderByCreatedAtDesc(Long courseId);
    @Query("SELECT cc FROM CourseComment cc JOIN FETCH cc.course WHERE cc.author.username = :username ORDER BY cc.createdAt DESC")
    List<CourseComment> findByAuthorUsernameOrderByCreatedAtDesc(@Param("username") String username);

    void deleteByAuthorUsername(String username);
}