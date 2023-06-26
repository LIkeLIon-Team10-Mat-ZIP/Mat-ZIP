package site.matzip.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.matzip.dto.MatzipCreationDTO;
import site.matzip.member.domain.Member;
import site.matzip.review.dto.ReviewCreationDTO;
import site.matzip.review.service.ReviewService;

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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@ModelAttribute ReviewCreationDTO reviewCreationDTO, @ModelAttribute MatzipCreationDTO matzipCreationDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "/review/create";
        }

        reviewService.create(matzipCreationDTO, reviewCreationDTO);
        return "redirect:/";
    }
}