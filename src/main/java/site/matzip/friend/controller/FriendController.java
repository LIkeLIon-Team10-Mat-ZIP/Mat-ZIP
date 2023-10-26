package site.matzip.friend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.matzip.base.rq.Rq;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.friend.dto.FriendDTO;
import site.matzip.friend.service.FriendService;
import site.matzip.member.domain.Member;

import java.util.List;

@Controller
@RequestMapping("/usr/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final Rq rq;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showList(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();

        List<FriendDTO> friendDTOS = friendService.convertToFriendDTOS(member);

        model.addAttribute("friendDTOS", friendDTOS);

        return "usr/friend/list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        friendService.delete(id);

        return rq.redirectWithMsg("/usr/member/myPage", "친구삭제가 완료되었습니다.");
    }
}
