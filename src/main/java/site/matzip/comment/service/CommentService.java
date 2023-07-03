package site.matzip.comment.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.matzip.comment.domain.Comment;
import site.matzip.comment.repository.CommentRepository;
import site.matzip.member.domain.Member;
import site.matzip.review.domain.Review;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void create(Review review, Member author, String content) {
        Comment comment = Comment.builder()
                .content(content)
                .build();

        comment.setReview(review);
        comment.setAuthor(author);
        commentRepository.save(comment);
    }

    public void remove(Comment comment) {
        commentRepository.delete(comment);
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException("Comment not Found"));
    }
}