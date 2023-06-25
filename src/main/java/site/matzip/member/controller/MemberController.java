package site.matzip.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.matzip.config.oauth.PrincipalOAuth2UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/member")
public class MemberController {

    @GetMapping("/")
    @ResponseBody
    public String test() {
        return "MEMBER TEST";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "usr/member/login";
    }

    @GetMapping("/login/oauth2/code/kakao")
    public String code(String code) {
        System.out.println("code COntroller = " + code);
        return "";
    }
}
