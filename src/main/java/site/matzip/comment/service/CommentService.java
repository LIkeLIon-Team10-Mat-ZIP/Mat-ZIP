package site.matzip.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.matzip.comment.domain.Comment;
import site.matzip.comment.dto.CommentCreationDTO;
import site.matzip.comment.repository.CommentRepository;
import site.matzip.member.domain.Member;
import site.matzip.member.repository.MemberRepository;
import site.matzip.review.domain.Review;
import site.matzip.review.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    public void create(CommentCreationDTO commentCreationDTO) {
        Member author = memberRepository.findById(commentCreationDTO.getAuthorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Review review = reviewRepository.findById(commentCreationDTO.getReviewId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        Comment comment = Comment.builder()
                .author(author)
                .review(review)
                .rating(commentCreationDTO.getRating())
                .title(commentCreationDTO.getTitle())
                .content(commentCreationDTO.getContent())
                .build();

        commentRepository.save(comment);
    }

    public void remove(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        commentRepository.delete(comment);
    }
}