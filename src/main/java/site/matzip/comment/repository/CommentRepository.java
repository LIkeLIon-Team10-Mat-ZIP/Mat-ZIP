package site.matzip.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}