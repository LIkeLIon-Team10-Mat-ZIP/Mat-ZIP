package site.matzip.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.comment.domain.Comment;
import site.matzip.member.domain.Member;
import site.matzip.review.domain.Review;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByAuthor(Member author);
}