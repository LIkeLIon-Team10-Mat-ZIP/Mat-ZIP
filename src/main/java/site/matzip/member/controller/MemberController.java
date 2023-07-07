package site.matzip.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.matzip.badge.service.MemberBadgeService;
import site.matzip.base.appConfig.AppConfig;
import site.matzip.base.rq.Rq;
import site.matzip.base.rsData.RsData;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.friend.dto.FriendDetailDTO;
import site.matzip.image.service.ProfileImageService;
import site.matzip.matzip.domain.MatzipMember;
import site.matzip.matzip.dto.MatzipInfoDTO;
import site.matzip.member.domain.Member;
import site.matzip.member.dto.MemberInfoDTO;
import site.matzip.member.dto.MemberProfileDTO;
import site.matzip.member.dto.MemberRankDTO;
import site.matzip.member.dto.NicknameUpdateDTO;
import site.matzip.member.service.MemberService;
import site.matzip.review.domain.Review;
import site.matzip.review.dto.MyReviewDTO;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/member")
public class MemberController {

    private final MemberService memberService;
    private final ProfileImageService profileImageService;
    private final MemberBadgeService memberBadgeService;
    private final AppConfig appConfig;
    private final Rq rq;

    @GetMapping("/login")
    public String login() {
        return "usr/member/login";
    }

    @PostMapping("/logout")
    public String logout(@AuthenticationPrincipal PrincipalDetails principalDetails,
                         HttpServletRequest request,
                         HttpServletResponse response) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
        // JSESSIONID 쿠키 삭제
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String showMyPage(Model model, @RequestParam(value = "menu", defaultValue = "1") int menu,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();

        String profileImageUrl = appConfig.getDefaultProfileImageUrl();
        if (member.getProfileImage() != null && member.getProfileImage().getImageUrl() != null) {
            profileImageUrl = member.getProfileImage().getImageUrl();
        }

        MemberInfoDTO memberInfoDTO = MemberInfoDTO.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImageUrl(profileImageUrl)
                .badgeImage(memberBadgeService.showMemberBadge(member))
                .build();

        model.addAttribute("memberInfoDTO", memberInfoDTO);

        switch (menu) {
            case 2:
                List<MyReviewDTO> myReviewDTOS = memberService.converToMyReviewDTO(member.getId());

                model.addAttribute("myReviewDTOS", myReviewDTOS);

                return "usr/member/myPage/review";
            case 3:
                List<FriendDetailDTO> friendDetailDTOS = memberService.converToFriendDetailDTO(member.getId());

                model.addAttribute("friendDetailDTOS", friendDetailDTOS);

                return "usr/member/myPage/friend";
            default:
                List<MatzipInfoDTO> matzipInfoDTOS = memberService.convertToMatzipInfoDTO(member.getId());

                model.addAttribute("matzipInfoDTOS", matzipInfoDTOS);

                return "usr/member/myPage/matzip";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage/modifyNickname")
    public String showModifyNickName() {
        return "usr/member/myPage/modifyNickname";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/myPage/modifyNickname")
    public String modifyNickName(NicknameUpdateDTO nicknameUpdateDTO, BindingResult result, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (result.hasErrors()) {
            return "/usr/member/myPage/modifyNickname";
        }

        RsData<Member> member = memberService.modifyNickname(principalDetails.getMember(), nicknameUpdateDTO);

        if (member.isFail()) {
            return rq.historyBack(member);
        }

        return rq.redirectWithMsg("/usr/member/myPage", member);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage/modifyProfileImage")
    public String showModifyProfileImage() {
        return "usr/member/myPage/modifyProfileImage";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/myPage/modifyProfileImage")
    public String modifyProfileImage(@RequestParam("profileImage") MultipartFile profileImage,
                                     @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        profileImageService.saveProfileImage(profileImage, principalDetails.getMember());

        return "redirect:/usr/member/myPage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/ranking")
    public String showRanking(Model model) {
        List<MemberRankDTO> memberRankDtoList = memberService.findAndConvertTopTenMember();

        model.addAttribute("memberRankDtoList", memberRankDtoList);

        return "/usr/member/ranking";
    }

    @GetMapping("/getProfile")
    @ResponseBody
    public MemberProfileDTO getProfile(@RequestParam String nickname) {
        Member member = memberService.findByNickname(nickname);
        String profileImageUrl = appConfig.getDefaultProfileImageUrl();
        if (member.getProfileImage() != null && member.getProfileImage().getImageUrl() != null) {
            profileImageUrl = member.getProfileImage().getImageUrl();
        }

        return MemberProfileDTO.builder()
                .profileImageUrl(profileImageUrl)
                .nickname(member.getNickname())
                .matzipCount(member.getMatzipMembers().size())
                .reviewCount(member.getReviews().size())
                .point(member.getPoint())
                .build();
    }
}