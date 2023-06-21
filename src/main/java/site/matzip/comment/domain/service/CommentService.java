package site.matzip.comment.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.matzip.comment.domain.Comment;
import site.matzip.comment.domain.CommentRepository;
import site.matzip.review.domain.Review;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void create(Review review, Double rating, String title, String content) {
        Comment comment = Comment.builder()
                .review(review)
                .rating(rating)
                .title(title)
                .content(content)
                .build();

        commentRepository.save(comment);
    }

    public void remove(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        commentRepository.delete(comment);
    }
}