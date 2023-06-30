package site.matzip.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.matzip.base.rq.Rq;
import site.matzip.base.rsData.RsData;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.image.service.ProfileImageService;
import site.matzip.member.domain.Member;
import site.matzip.member.dto.MemberInfoDTO;
import site.matzip.member.dto.NicknameUpdateDTO;
import site.matzip.member.service.MemberService;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/member")
public class MemberController {

    private final MemberService memberService;
    private final ProfileImageService profileImageService;
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
    public String showMyPage(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();

        String profileImageUrl = "https://grooveobucket.s3.ap-northeast-2.amazonaws.com/albumCover/free-icon-user-5264565.png";
        if (member.getProfileImage() != null && member.getProfileImage().getImageUrl() != null) {
            profileImageUrl = member.getProfileImage().getImageUrl();
        }

        MemberInfoDTO memberInfoDTO = MemberInfoDTO.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImageUrl(profileImageUrl)
                .build();

        model.addAttribute("memberInfoDTO", memberInfoDTO);
        return "usr/member/myPage";
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

}
