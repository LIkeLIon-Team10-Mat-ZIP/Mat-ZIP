package site.matzip.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.member.service.MemberService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    @ResponseBody
    public String test() {
        return "MEMBER TEST";
    }

    @GetMapping("/login")
    public String login() {
        return "usr/member/login";
    }

    //@PreAuthorize("isAuthenticated()")
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

        memberService.logout(principalDetails.getMember().getId());

        return "redirect:/";
    }
}
