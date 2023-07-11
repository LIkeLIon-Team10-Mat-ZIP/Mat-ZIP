package site.matzip.friend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.friend.dto.FriendDTO;
import site.matzip.friend.service.FriendService;
import site.matzip.member.domain.Member;
import site.matzip.member.service.MemberService;

import java.util.List;

@Controller
@RequestMapping("/usr/friends")
@RequiredArgsConstructor
public class FriendController {
    private final MemberService memberService;
    private final FriendService friendService;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showList(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = memberService.findByUsername("user1");  // TODO: 추후에 principalDetails.getMember()로 변경

        List<FriendDTO> friendDTOS = friendService.convertToFriendDTOS(member);

        model.addAttribute("friendDTOS", friendDTOS);

        return "usr/friend/list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        friendService.delete(id);
        return "redirect:/usr/member/myPage?menu=3";
    }
}
