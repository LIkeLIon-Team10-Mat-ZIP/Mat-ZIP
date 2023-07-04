package site.matzip.comment.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import site.matzip.base.event.EventAfterComment;
import site.matzip.comment.domain.Comment;
import site.matzip.comment.repository.CommentRepository;
import site.matzip.member.domain.Member;
import site.matzip.review.domain.Review;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ApplicationEventPublisher publisher;

    public void create(Review review, Member author, String content) {
        Comment comment = Comment.builder()
                .content(content)
                .build();

        comment.setReview(review);
        comment.setAuthor(author);
        commentRepository.save(comment);

        if (!review.getAuthor().getNickname().equals(author.getNickname())) {
            // 본인 리뷰에 본인이 댓글을 단 경우를 제외하고 이벤트 발행
            publisher.publishEvent(new EventAfterComment(this, review.getAuthor(), author));
        }
    }

    public void remove(Comment comment) {
        commentRepository.delete(comment);
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException("Comment not Found"));
    }
}