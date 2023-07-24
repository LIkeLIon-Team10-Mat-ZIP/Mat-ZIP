package site.matzip.review.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.matzip.badge.service.MemberBadgeService;
import site.matzip.base.rq.Rq;
import site.matzip.comment.domain.Comment;
import site.matzip.comment.dto.CommentInfoDTO;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.image.domain.ReviewImage;
import site.matzip.image.service.ReviewImageService;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.dto.MatzipInfoDTO;
import site.matzip.matzip.service.MatzipService;
import site.matzip.review.domain.Review;
import site.matzip.review.dto.ReviewCreationDTO;
import site.matzip.review.dto.ReviewDetailDTO;
import site.matzip.review.dto.ReviewListDTO;
import site.matzip.review.service.ReviewService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;
    private final MatzipService matzipService;
    private final MemberBadgeService memberBadgeService;
    private final Rq rq;

    // 맛집과 함께 생성
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create() {
        return "review/create";
    }

    // 리뷰만 생성
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/{matzipId}")
    public String create(Model model, @PathVariable Long matzipId) {
        Matzip matzip = matzipService.findById(matzipId);
        MatzipInfoDTO matzipInfoDTO = new MatzipInfoDTO(matzip);
        ReviewCreationDTO reviewCreationDTO = new ReviewCreationDTO();
        model.addAttribute("matzipInfoDTO", matzipInfoDTO);
        model.addAttribute("reviewCreationDTO", reviewCreationDTO);
        model.addAttribute("create", true);

        return "review/add";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{matzipId}")
    public String create(@PathVariable Long matzipId,
                         @ModelAttribute @Valid ReviewCreationDTO reviewCreationDTO,
                         BindingResult result,
                         @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        if (result.hasErrors()) {
            return rq.historyBack("리뷰등록에 올바른 값이 아닙니다.");
        }

        Matzip matzip = matzipService.findById(matzipId);
        Long authorId = principalDetails.getMember().getId();

        Review createdReview = reviewService.create(reviewCreationDTO, authorId, matzip);
        reviewImageService.create(reviewCreationDTO.getImageFiles(), createdReview);

        return rq.redirectWithMsg("/main", "리뷰가 등록되었습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{reviewId}")
    public String modify(@PathVariable Long reviewId, ReviewCreationDTO reviewCreationDTO, Model model,
                         @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Review review = reviewService.findById(reviewId);
        Matzip matzip = review.getMatzip();
        MatzipInfoDTO matzipInfoDTO = new MatzipInfoDTO(matzip);

        reviewService.checkAccessPermission(reviewId, principalDetails);

        model.addAttribute("matzipInfoDTO", matzipInfoDTO);
        reviewCreationDTO.setRating(review.getRating());
        reviewCreationDTO.setContent(review.getContent());
        reviewCreationDTO.setImageUrls(review.getReviewImages()
                .stream()
                .map(ReviewImage::getImageUrl)
                .collect(Collectors.toList()));

        return "review/add";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{reviewId}")
    public String modify(@PathVariable Long reviewId,
                         @ModelAttribute @Valid ReviewCreationDTO reviewCreationDTO,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        Review review = reviewService.findById(reviewId);
        reviewService.checkAccessPermission(reviewId, principalDetails);

        if (bindingResult.hasErrors()) {
            return rq.historyBack("리뷰 수정에 올바른 형식이 아닙니다.");
        }

        Review modifyReview = reviewService.modify(review, reviewCreationDTO);

        if (!reviewService.isImageFileEmpty(reviewCreationDTO)) {
            reviewImageService.modify(reviewCreationDTO.getImageFiles(), modifyReview);
        }

        return rq.redirectWithMsg("/review/detail/" + reviewId, "리뷰 수정이 완료되었습니다.");
    }

    @GetMapping("/api/list/{matzipId}")
    @ResponseBody
    public ResponseEntity<Page<ReviewListDTO>> getReviewsByMatzipId(@PathVariable Long matzipId,
                                                                    @RequestParam int pageSize,
                                                                    @RequestParam int pageNumber) {
        Page<ReviewListDTO> reviews = reviewService.findByMatzipIdAndConvertToDTO(matzipId, pageSize, pageNumber);

        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/api/mylist/{matzipId}")
    public ResponseEntity<Page<ReviewListDTO>> getReviewsByMatzipIdAndAuthor(@PathVariable Long matzipId,
                                                                             @RequestParam int pageSize,
                                                                             @RequestParam int pageNumber,
                                                                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long authorId = principalDetails.getMember().getId();
        Page<ReviewListDTO> reviews = reviewService.findByMatzipIdWithAuthorAndConvertToReviewDTO(matzipId, authorId, pageSize, pageNumber);

        return ResponseEntity.ok(reviews);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{reviewId}")
    public String delete(@PathVariable Long reviewId, @AuthenticationPrincipal PrincipalDetails principalDetail) {
        Review review = reviewService.findById(reviewId);

        reviewService.checkAccessPermission(reviewId, principalDetail);
        reviewService.remove(review);
        reviewImageService.remove(review);

        return rq.redirectWithMsg("/main", "리뷰 삭제가 완료되었습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail/{reviewId}")
    public String detail(Model model, @PathVariable Long reviewId, @AuthenticationPrincipal PrincipalDetails principalDetails,
                         HttpServletRequest request, HttpServletResponse response) {
        Review review = reviewService.findById(reviewId);
        ReviewDetailDTO reviewDetailDTO = reviewService.convertToReviewDetailDTO(reviewId, principalDetails.getMember().getId());
        reviewDetailDTO.setHeart(reviewService.isHeart(principalDetails.getMember(), review));
        reviewDetailDTO.setBadgeImage(memberBadgeService.showMemberBadge(review.getAuthor()));
        List<Comment> comments = review.getComments();
        List<CommentInfoDTO> commentInfoDTOS = reviewService.convertToCommentInfoDTOS(comments, principalDetails.getMember().getId());

        model.addAttribute("reviewDetailDTO", reviewDetailDTO);
        model.addAttribute("commentInfoDTOS", commentInfoDTOS);

        reviewService.updateViewCountWithCookie(review, request, response, principalDetails.getUserId());

        return "review/detail";
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