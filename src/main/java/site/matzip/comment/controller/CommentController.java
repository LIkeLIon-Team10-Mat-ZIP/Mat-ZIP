package site.matzip.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.matzip.comment.domain.Comment;
import site.matzip.comment.service.CommentService;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.member.domain.Member;
import site.matzip.review.domain.Review;
import site.matzip.review.service.ReviewService;

import java.util.Objects;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final ReviewService reviewService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String create(@PathVariable Long id, String content, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Review review = reviewService.findByID(id);
        Member author = principalDetails.getMember();

        commentService.create(review, author, content);
        return "redirect:/review/detail/%d".formatted(id);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("{id}")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Comment comment = commentService.findById(id);
        Long reviewId = comment.getReview().getId();

        if (!Objects.equals(comment.getAuthor().getId(), principalDetails.getMember().getId())) {
            throw new AccessDeniedException("You do not have permission to delete.");
        }

        commentService.remove(comment);
        return "redirect:/review/detail/%d".formatted(reviewId);
    }
}