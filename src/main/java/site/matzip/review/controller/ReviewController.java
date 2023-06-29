package site.matzip.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.matzip.matzip.dto.MatzipReviewDTO;
import site.matzip.review.domain.Review;
import site.matzip.review.service.ReviewService;

import java.util.List;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("matzipReviewDTO", new MatzipReviewDTO());

        return "/review/createDev";
    }

    @GetMapping("/api/{matzipId}")
    @ResponseBody
    public ResponseEntity<List<Review>> getReviewsByMatzip(@PathVariable Long matzipId) {
        List<Review> reviews = reviewService.getReviewsByMatzip(matzipId);
        return ResponseEntity.ok(reviews);
    }
}