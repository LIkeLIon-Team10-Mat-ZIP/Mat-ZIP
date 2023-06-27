package site.matzip.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.matzip.review.dto.ReviewCreationDTO;
import site.matzip.review.service.ReviewService;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("reviewCreationDTO", new ReviewCreationDTO());

        return "/review/create";
    }
}