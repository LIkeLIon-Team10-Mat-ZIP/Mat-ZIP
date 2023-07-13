package site.matzip.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import site.matzip.friend.dto.FriendDetailDTO;
import site.matzip.image.service.ProfileImageService;
import site.matzip.matzip.dto.MatzipInfoDTO;
import site.matzip.member.domain.Member;
import site.matzip.member.dto.*;
import site.matzip.member.service.MemberService;
import site.matzip.review.dto.MyReviewDTO;

import java.io.IOException;
import java.util.List;

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

        return "redirect:/main";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String showMyPage(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long MemberId = principalDetails.getUserId();

        MemberInfoDTO memberInfoDTO = memberService.convertToMemberInfoDTO(MemberId);
        MemberInfoCntDTO memberInfoCntDTO = memberService.convertToMemberInfoCntDTO(MemberId);

        model.addAttribute("memberInfoCntDTO", memberInfoCntDTO);
        model.addAttribute("memberInfoDTO", memberInfoDTO);

        List<MyReviewDTO> myReviewDTOS = memberService.converToMyReviewDTO(MemberId);
        model.addAttribute("myReviewDTOS", myReviewDTOS);

        List<FriendDetailDTO> friendDetailDTOS = memberService.converToFriendDetailDTO(MemberId);
        model.addAttribute("friendDetailDTOS", friendDetailDTOS);

        List<MemberRankDTO> memberRankDTOS = memberService.findAndConvertTenMemberAroundMember(MemberId);
        MemberPointDTO memberPointDTO = memberService.convertToMemberPointDTO(MemberId);
        model.addAttribute("memberPointDTO", memberPointDTO);
        model.addAttribute("memberRankDTOS", memberRankDTOS);

        List<MatzipInfoDTO> matzipInfoDTOS = memberService.convertToMatzipInfoDTO(MemberId);
        model.addAttribute("matzipInfoDTOS", matzipInfoDTOS);

        return "usr/member/myPage2";
    }

    @GetMapping("/myPage/friendMap")
    public String getFriendMap() {
        return "usr/member/myPage/friendMap";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage/modifyNickname")
    public String showModifyNickName() {
        return "usr/member/myPage/modifyNickname";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/myPage/modifyNickname")
    public String modifyNickName(@Valid NicknameUpdateDTO nicknameUpdateDTO,
                                 BindingResult result,
                                 @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (result.hasErrors()) {
            return "usr/member/myPage/modifyNickname";
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

        return rq.redirectWithMsg("/usr/member/myPage", "프로필 이미지가 변경되었습니다.");
    }

    @GetMapping("/getProfile")
    @ResponseBody
    public MemberProfileDTO getProfile(@RequestParam String nickname) {
        return memberService.convertToMemberProfileDTO(nickname);
    }
}