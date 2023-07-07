package site.matzip.review.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import site.matzip.image.service.ReviewImageService;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.dto.MatzipInfoDTO;
import site.matzip.matzip.service.MatzipService;
import site.matzip.review.domain.Review;
import site.matzip.review.dto.ReviewCreationDTO;
import site.matzip.review.dto.ReviewDetailDTO;
import site.matzip.review.service.ReviewService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;
    private final MatzipService matzipService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create() {
        return "/review/create";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/{matzipId}")
    public String create(Model model, @PathVariable Long matzipId) {
        Matzip matzip = matzipService.findById(matzipId);
        MatzipInfoDTO matzipInfoDTO = new MatzipInfoDTO(matzip);
        ReviewCreationDTO reviewCreationDTO = new ReviewCreationDTO();
        model.addAttribute("matzipInfoDTO", matzipInfoDTO);
        model.addAttribute("reviewCreationDTO", reviewCreationDTO);

        return "/review/add";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{matzipId}")
    public String create(@PathVariable Long matzipId,
                         @ModelAttribute ReviewCreationDTO reviewCreationDTO,
                         BindingResult result,
                         @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        if (result.hasErrors()) {
            return "/review/add";
        }
        Matzip matzip = matzipService.findById(matzipId);
        Long authorId = principalDetails.getMember().getId();

        Review createdReview = reviewService.create(reviewCreationDTO, authorId, matzip);
        reviewImageService.create(reviewCreationDTO.getImageFiles(), createdReview);
        return "redirect:/";
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
        Review review = reviewService.findById(id);

        if (!Objects.equals(review.getAuthor().getId(), principalDetail.getMember().getId())) {
            throw new AccessDeniedException("You do not have permission to delete.");
        }
        reviewService.remove(review);

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails,
                         HttpServletRequest request, HttpServletResponse response) {
        Review review = reviewService.findById(id);
        ReviewDetailDTO reviewDetailDTO = reviewService.convertToReviewDetailDTO(id, principalDetails.getMember().getId());
        reviewDetailDTO.setHeart(reviewService.isHeart(principalDetails.getMember(), review));
        List<Comment> comments = review.getComments();
        List<CommentInfoDTO> commentInfoDTOS = reviewService.convertToCommentInfoDTOS(comments, principalDetails.getMember().getId());

        model.addAttribute("reviewDetailDTO", reviewDetailDTO);
        model.addAttribute("commentInfoDTOS", commentInfoDTOS);

        reviewService.updateViewCountWithCookie(review, request, response);

        return "/review/detail";
    }

    @GetMapping("/getViewCount")
    @ResponseBody
    public String getViewCount(@RequestParam Long reviewId) {
        return String.valueOf(reviewService.getViewCount(reviewId));
    }

    @GetMapping("/getHeartCount")
    @ResponseBody
    public String getHeartCount(@RequestParam Long reviewId) {
        return String.valueOf(reviewService.getHeartCount(reviewId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/updateHeart")
    @ResponseBody
    public ResponseEntity<Long> updateHeart(@RequestParam Long reviewId,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        reviewService.updateHeart(principalDetails.getMember().getId(), reviewId);

        return ResponseEntity.ok(reviewId);
    }
}