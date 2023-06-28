package site.matzip.comment.service;

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
                .review(review)
                .author(author)
                .content(content)
                .build();

        commentRepository.save(comment);
    }
}