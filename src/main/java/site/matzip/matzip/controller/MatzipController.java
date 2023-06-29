package site.matzip.matzip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.matzip.base.rsData.RsData;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.domain.MatzipRecommendation;
import site.matzip.matzip.dto.MatzipCreationDTO;
import site.matzip.matzip.dto.MatzipListDTO;
import site.matzip.matzip.dto.MatzipReviewDTO;
import site.matzip.matzip.service.MatzipService;
import site.matzip.member.domain.Member;
import site.matzip.member.service.MemberService;
import site.matzip.review.dto.ReviewCreationDTO;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/matzip")
@RequiredArgsConstructor
public class MatzipController {
    private final MatzipService matzipService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@RequestBody MatzipCreationDTO matzipCreationDTO, BindingResult result, Authentication authentication) {
        if (result.hasErrors()) {
            return "/matzip/create";
        }
        Member author = getMember(authentication);
        matzipService.create(matzipCreationDTO, author);
        return "redirect:/matzip/list";
    }

    private Member getMember(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Member author = null;

        if (principal instanceof PrincipalDetails) {
            // 주어진 PrincipalDetails 객체 사용
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            author = principalDetails.getMember();
        } else if (principal instanceof UserDetails) {
            // 주어진 Authentication 객체를 사용하고 UserDetails 중에서 member 찾기
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            author = memberService.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("username(%s) not found".formatted(username)));
        }
        return author;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/createWithReview")
    public String createWithReview(@ModelAttribute MatzipReviewDTO matzipReviewDTO, BindingResult result, Authentication authentication) {
        if (result.hasErrors()) {
            return "/matzip/create";
        }
        MatzipCreationDTO matzipCreationDTO = matzipReviewDTO.getMatzipCreationDTO();
        ReviewCreationDTO reviewCreationDTO = matzipReviewDTO.getReviewCreationDTO();

        Member author = getMember(authentication);

        RsData<Matzip> savedMatzip = matzipService.create(matzipCreationDTO, reviewCreationDTO, author);

        return "redirect:/matzip/list";
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
    public ResponseEntity<List<MatzipListDTO>> search() {
        List<Matzip> matzipList = matzipService.findAllWithRecommendations();
        List<MatzipListDTO> matzipDtoList = matzipList.stream().map(matzip -> {
            List<MatzipRecommendation> recommendations = matzip.getRecommendations();
            double rating = 0;
            String description = "";
            if (!recommendations.isEmpty()) {
                MatzipRecommendation recommendation = recommendations.get(0); // 첫 번째 평가 정보 사용함. 이거 내가 작성한걸로 나오게 바꿔야함
                rating = recommendation.getRating();
                description = recommendation.getDescription();
            }
            return MatzipListDTO.builder()
                    .matzipId(matzip.getId())
                    .matzipName(matzip.getMatzipName())
                    .address(matzip.getAddress())
                    .phoneNumber(matzip.getPhoneNumber())
                    .matzipUrl(matzip.getMatzipUrl())
                    .matzipType(matzip.getMatzipType())
                    .x(matzip.getX())
                    .y(matzip.getY())
                    .rating(rating)
                    .description(description)
                    .build();
        }).collect(Collectors.toList());

        return ResponseEntity.ok(matzipDtoList);
    }
}
