package site.matzip.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.service.MatzipService;
import site.matzip.member.domain.Member;
import site.matzip.review.domain.Review;
import site.matzip.review.dto.ReviewCreationDTO;
import site.matzip.review.service.ReviewService;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final MatzipService matzipService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create() {
        return "/review/create";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/{id}")
    public String create(Model model, @PathVariable Long id, ReviewCreationDTO reviewCreationDTO) {
        Matzip matzip = matzipService.findMatzip(id);
        model.addAttribute("matzip", matzip);
        model.addAttribute("reviewCreationDTO", reviewCreationDTO);
        return "/review/add";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String create(@PathVariable Long id, ReviewCreationDTO reviewCreationDTO,
                         BindingResult result, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (result.hasErrors()) {
            return "/review/add";
        }

        Matzip matzip = matzipService.findMatzip(id);
        Member author = principalDetails.getMember();

        reviewService.create(reviewCreationDTO, author, matzip);
        return "redirect:/matzip/list";
    }

    @GetMapping("/api/{matzipId}")
    @ResponseBody
    public ResponseEntity<List<Review>> getReviewsByMatzip(@PathVariable Long matzipId) {
        List<Review> reviews = reviewService.getReviewsByMatzip(matzipId);
        return ResponseEntity.ok(reviews);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetail) {
        Review review = reviewService.findReview(id);

        if (!Objects.equals(review.getAuthor().getId(), principalDetail.getMember().getId())) {
            throw new AccessDeniedException("You do not have permission to delete.");
        }

        reviewService.remove(review);
        return "redirect:/matzip/list";
    }
}