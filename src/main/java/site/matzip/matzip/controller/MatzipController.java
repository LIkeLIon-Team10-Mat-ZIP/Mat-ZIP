package site.matzip.matzip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.matzip.base.rq.Rq;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.dto.MatzipCreationDTO;
import site.matzip.matzip.dto.MatzipListDTO;
import site.matzip.matzip.dto.MatzipReviewDTO;
import site.matzip.matzip.dto.MatzipReviewListDTO;
import site.matzip.matzip.service.MatzipService;
import site.matzip.member.domain.Member;
import site.matzip.review.dto.ReviewCreationDTO;
import site.matzip.review.dto.ReviewListDTO;
import site.matzip.review.service.ReviewService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/matzip")
@RequiredArgsConstructor
public class MatzipController {
    private final MatzipService matzipService;
    private final ReviewService reviewService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity create(@RequestBody MatzipCreationDTO matzipCreationDTO, BindingResult result, Authentication authentication) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        Member author = rq.getMember(authentication);
        matzipService.create(matzipCreationDTO, author);
        return ResponseEntity.ok("Matzip created successfully");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/createWithReview")
    public ResponseEntity createWithReview(@RequestBody MatzipReviewDTO matzipReviewDTO, BindingResult result, Authentication authentication) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        MatzipCreationDTO matzipCreationDTO = matzipReviewDTO.getMatzipCreationDTO();
        ReviewCreationDTO reviewCreationDTO = matzipReviewDTO.getReviewCreationDTO();
        Member author = rq.getMember(authentication);
        matzipService.create(matzipCreationDTO, reviewCreationDTO, author);
        return ResponseEntity.ok("맛집과 리뷰가 생성되었습니다.");
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Matzip> matzipList = matzipService.findAll();
        List<MatzipListDTO> matzipDtoList = matzipList.stream().map(matzip -> MatzipListDTO.builder().matzipName(matzip.getMatzipName()).address(matzip.getAddress()).phoneNumber(matzip.getPhoneNumber()).matzipType(matzip.getMatzipType()).build()).collect(Collectors.toList());
        model.addAttribute("matzipList", matzipDtoList);
        return "/matzip/list";
    }

    @GetMapping("/api/list")
    @ResponseBody
    public ResponseEntity<List<MatzipReviewListDTO>> searchAllWithReviews(Authentication authentication) {
        try {
            List<MatzipListDTO> matzipDtoList = matzipService.showAllDTO(rq.getMember(authentication).getId());
            List<ReviewListDTO> reviewDtoList = reviewService.findAll();
            List<MatzipReviewListDTO> matzipReviewDtoList = matzipService.mergeMatzipAndReviews(matzipDtoList, reviewDtoList);
            return ResponseEntity.ok(matzipReviewDtoList);
        } catch (Exception e) {
            // 예외 발생 시 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @GetMapping("/api/mylist")
//    @ResponseBody
//    public ResponseEntity<List<MatzipListDTO>> searchMine(Authentication authentication) {
//        List<MatzipListDTO> matzipList = matzipService.showAll(rq.getMember(authentication).getId());
//
//        return ResponseEntity.ok();
//    }
}
