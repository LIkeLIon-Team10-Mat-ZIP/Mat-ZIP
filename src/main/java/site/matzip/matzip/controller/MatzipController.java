package site.matzip.matzip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.matzip.base.rq.Rq;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.image.service.ReviewImageService;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.dto.MatzipCreationDTO;
import site.matzip.matzip.dto.MatzipListDTO;
import site.matzip.matzip.dto.MatzipReviewDTO;
import site.matzip.matzip.dto.MatzipReviewListDTO;
import site.matzip.matzip.service.MatzipService;
import site.matzip.member.domain.Member;
import site.matzip.review.domain.Review;
import site.matzip.review.dto.ReviewCreationDTO;
import site.matzip.review.dto.ReviewListDTO;
import site.matzip.review.service.ReviewService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/matzip")
@RequiredArgsConstructor
public class MatzipController {
    private final MatzipService matzipService;
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;
    private final Rq rq;

    @GetMapping("create")
    public String create() {
        return "/matzip/create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@RequestBody MatzipCreationDTO matzipCreationDTO, BindingResult result, Authentication authentication) {
        Member author = rq.getMember(authentication);
        matzipService.create(matzipCreationDTO, author.getId());

        return "redirect:/matzip/mylist";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/createWithReview")
    public String createWithReview(@ModelAttribute MatzipReviewDTO matzipReviewDTO,
                                   BindingResult result,
                                   @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        MatzipCreationDTO matzipCreationDTO = matzipReviewDTO.getMatzipCreationDTO();
        ReviewCreationDTO reviewCreationDTO = matzipReviewDTO.getReviewCreationDTO();
        Long authorId = principalDetails.getMember().getId();
        Member author = principalDetails.getMember();

        Matzip createdMatzip = matzipService.create(matzipCreationDTO, authorId);
        Review createdReview = reviewService.create(reviewCreationDTO, authorId, createdMatzip);
        reviewImageService.create(reviewCreationDTO.getImageFiles(), createdReview);

        return rq.redirectWithMsg("/", "맛집과 리뷰가 등록되었습니다.");
    }

    @GetMapping("/list")
    public String list(Model model) {
        return "/matzip/list";
    }

    @GetMapping("/mylist")
    public String showMyList() {
        return "/matzip/mylist";
    }

    @GetMapping("/api/list")
    @ResponseBody
    public ResponseEntity<List<MatzipReviewListDTO>> searchAllWithReviews(Authentication authentication) {
        try {
            List<MatzipListDTO> matzipDtoList = matzipService.findAndConvertAll(rq.getMember(authentication).getId());
            List<ReviewListDTO> reviewDtoList = reviewService.findAndConvertAll();
            List<MatzipReviewListDTO> matzipReviewDtoList = matzipService.mergeMatzipAndReviews(matzipDtoList, reviewDtoList);

            return ResponseEntity.ok(matzipReviewDtoList);
        } catch (Exception e) {
            // 예외 발생 시 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/api/mylist")
    @ResponseBody
    public ResponseEntity<List<MatzipReviewListDTO>> searchMineWithReviews(Authentication authentication) {
        try {
            List<MatzipListDTO> matzipDtoList = matzipService.findAndConvertMine(rq.getMember(authentication).getId());
            List<ReviewListDTO> reviewDtoList = reviewService.findAndConvertMine(rq.getMember(authentication).getId());
            List<MatzipReviewListDTO> matzipReviewDtoList = matzipService.mergeMatzipAndReviews(matzipDtoList, reviewDtoList);
            return ResponseEntity.ok(matzipReviewDtoList);
        } catch (Exception e) {
            // 예외 발생 시 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}