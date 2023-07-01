package site.matzip.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.matzip.comment.domain.Comment;
import site.matzip.comment.dto.CommentInfoDTO;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.dto.MatzipInfoDTO;
import site.matzip.matzip.service.MatzipService;
import site.matzip.member.domain.Member;
import site.matzip.review.domain.Review;
import site.matzip.review.dto.ReviewCreationDTO;
import site.matzip.review.dto.ReviewDetailDTO;
import site.matzip.review.service.ReviewService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final MatzipService matzipService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create() {
        return "/review/createDev";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/{matzipId}")
    public String create(Model model, @PathVariable Long matzipId, ReviewCreationDTO reviewCreationDTO) {
        Matzip matzip = matzipService.findById(matzipId);
        MatzipInfoDTO matzipInfoDTO = new MatzipInfoDTO(matzip);

        model.addAttribute("matzipInfoDTO", matzipInfoDTO);
        model.addAttribute("reviewCreationDTO", reviewCreationDTO);
        return "/review/add";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{matzipId}")
    public String create(@PathVariable Long matzipId, ReviewCreationDTO reviewCreationDTO,
                         BindingResult result, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (result.hasErrors()) {
            return "/review/add";
        }

        Matzip matzip = matzipService.findById(matzipId);
        Member author = principalDetails.getMember();

        reviewService.create(reviewCreationDTO, author, matzip);
        return "redirect:/matzip/list";
    }

    @GetMapping("/api/{matzipId}")
    @ResponseBody
    public ResponseEntity<List<Review>> getReviewsByMatzipId(@PathVariable Long matzipId, @RequestParam int pageSize, @RequestParam int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Review> reviews = reviewService.findByMatzipId(matzipId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetail) {
        Review review = reviewService.findByID(id);

        if (!Objects.equals(review.getAuthor().getId(), principalDetail.getMember().getId())) {
            throw new AccessDeniedException("You do not have permission to delete.");
        }

        reviewService.remove(review);
        return "redirect:/matzip/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Long id) {
        Review review = reviewService.findByID(id);
        Matzip matzip = review.getMatzip();
        // Comment
        List<Comment> comments = review.getComments();
        List<CommentInfoDTO> commentInfoDTOS = comments.stream()
                .map(
                        comment -> CommentInfoDTO.builder()
                                .authorNickname(comment.getAuthor().getNickname())
                                .createDate(comment.getCreateDate())
                                .content(comment.getContent())
                                .build())
                .collect(Collectors.toList());

        ReviewDetailDTO reviewDetailDTO = new ReviewDetailDTO(review, matzip);
        model.addAttribute("reviewDetailDTO", reviewDetailDTO);
        model.addAttribute("commentInfoDTOS", commentInfoDTOS);
        return "/review/detail";
    }
}