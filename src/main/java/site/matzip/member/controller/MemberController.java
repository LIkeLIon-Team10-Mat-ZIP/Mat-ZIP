package site.matzip.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
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
}
