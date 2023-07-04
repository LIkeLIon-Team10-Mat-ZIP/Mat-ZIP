package site.matzip.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.matzip.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c from Comment c WHERE c.createDate < :olderThanTime")
    List<Comment> findCommentsOlderThan(LocalDateTime olderThanTime);
}