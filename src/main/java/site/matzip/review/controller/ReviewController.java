package site.matzip.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.matzip.dto.MatzipCreationDTO;
import site.matzip.member.domain.Member;
import site.matzip.review.domain.Review;
import site.matzip.review.dto.ReviewCreationDTO;
import site.matzip.review.service.ReviewService;

import java.util.List;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member author = principalDetails.getMember();

        model.addAttribute("reviewCreationDTO", new ReviewCreationDTO());
        model.addAttribute("author", author);

        return "/review/create";
    }

    @GetMapping("/api/{matzipId}")
    @ResponseBody
    public ResponseEntity<List<Review>> getReviewsByMatzip(@PathVariable Long matzipId) {
        List<Review> reviews = reviewService.getReviewsByMatzip(matzipId);
        return ResponseEntity.ok(reviews);
    }
}