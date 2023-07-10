package site.matzip.friendRequest.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.matzip.base.event.EventAfterComment;
import site.matzip.base.rq.Rq;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.friendRequest.dto.FriendRequestDTO;
import site.matzip.friendRequest.entity.FriendRequest;
import site.matzip.friendRequest.service.FriendRequestService;
import site.matzip.member.domain.Member;
import site.matzip.member.service.MemberService;

import java.util.List;

@Controller
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendRequestController {
    private final FriendRequestService friendRequestService;
    private final ApplicationEventPublisher publisher;
    private final MemberService memberService;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showList(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = memberService.findByUsername("user1").orElseThrow(() -> new EntityNotFoundException("member not found"));
        List<FriendRequestDTO> friendRequestDTOS = friendRequestService.convertToFriendRequestDTOS(member);

        model.addAttribute("friendRequestDTOS", friendRequestDTOS);

        return "usr/friend/requestList";
    }

    @GetMapping("/add")
    public String showAddFriendForm() {
        return "usr/friend/requestForm";
    }

    @PostMapping("/add")
    public String addFriend(@AuthenticationPrincipal PrincipalDetails principalDetails, Member fromMember) {
        Member toMember = principalDetails.getMember();

        friendRequestService.addFriendRequest(toMember, fromMember);

        return "redirect:/friends/list";
    }

    @PostMapping("/accept")
    public String acceptFriendRequest(@RequestParam("friendRequestId") Long friendRequestId) {
        FriendRequest friendRequest = friendRequestService.getFriendRequest(friendRequestId)
                .orElseThrow(() -> new EntityNotFoundException("FriendRequest not found."));

        Member member1 = friendRequest.getToMember();
        Member member2 = friendRequest.getFromMember();

        publisher.publishEvent(new EventAfterComment(this, member1, member2)); // 친구 요청 수락 시 이벤트 발행

        friendRequestService.deleteRequest(friendRequestId); // 요청 삭제

        return "redirect:/friends/list";
    }

    @PostMapping("/reject")
    public String rejectFriendRequest(@RequestParam("friendRequestId") Long friendRequestId) {
        friendRequestService.deleteRequest(friendRequestId);

        return "redirect:/friends/list";
    }
}
