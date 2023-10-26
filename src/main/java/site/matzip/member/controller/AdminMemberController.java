package site.matzip.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.member.domain.Member;
import site.matzip.member.service.AdminMemberService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/member")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @GetMapping("insert")
    @ResponseBody
    @Transactional
    public String insert() {
        Member member = adminMemberService.insert();

        return member.getRole().toString();
    }

    @GetMapping
    @ResponseBody
    public String currentUsername(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        return principalDetails.getUsername();
    }

    @GetMapping("test")
    public String test() {
        return "/usr/member/test";

    }
}
