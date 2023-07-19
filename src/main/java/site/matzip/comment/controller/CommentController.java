package site.matzip.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.matzip.base.rq.Rq;
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
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{reviewId}")
    public String create(@PathVariable Long reviewId, String content, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        commentService.create(reviewId, principalDetails.getMember().getId(), content);

        return rq.redirectWithMsg("review/detail/%d".formatted(reviewId), "댓글 등록이 완료되었습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Comment comment = commentService.findById(id);
        Long reviewId = comment.getReview().getId();

        commentService.checkAccessPermission(principalDetails.getMember().getId(), comment);
        commentService.remove(comment);

        return rq.redirectWithMsg("review/detail/%d".formatted(reviewId), "댓글 삭제가 완료되었습니다.");
    }
}