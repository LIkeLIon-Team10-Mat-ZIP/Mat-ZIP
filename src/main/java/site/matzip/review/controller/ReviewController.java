package site.matzip.review.controller;

import lombok.RequiredArgsConstructor;
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
import site.matzip.review.dto.ReviewCreationDTO;
import site.matzip.review.service.ReviewService;

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
}