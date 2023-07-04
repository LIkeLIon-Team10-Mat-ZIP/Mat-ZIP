package site.matzip.comment.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.matzip.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {"author"})
    @Query("SELECT c from Comment c WHERE c.createDate < :olderThanTime")
    List<Comment> findCommentsOlderThan(@Param("olderThanTime") LocalDateTime olderThanTime);
}