package site.matzip.friendRequest.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.matzip.base.event.EventAfterFriendRequestAccept;
import site.matzip.base.rq.Rq;
import site.matzip.base.rsData.RsData;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.friendRequest.dto.FriendRequestDTO;
import site.matzip.friendRequest.domain.FriendRequest;
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
    private final Rq rq;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showList(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();

        List<FriendRequestDTO> friendRequestDTOS = friendRequestService.convertToFriendRequestDTOS(member);

        model.addAttribute("friendRequestDTOS", friendRequestDTOS);

        return "usr/friend/requestList";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addFriend(@AuthenticationPrincipal PrincipalDetails principalDetails, String nickname) {
        Member fromMember = principalDetails.getMember();

        RsData<FriendRequest> friendRequestRsData = friendRequestService.checkRequestAdmin(nickname, fromMember.getNickname());

        if (friendRequestRsData.isFail()) {
            return new ResponseEntity<>(friendRequestRsData.getMsg(), HttpStatus.OK);
        }

        Member toMember = friendRequestService.getMember(nickname);
        friendRequestService.addFriendRequest(toMember, fromMember);

        return new ResponseEntity<>(friendRequestRsData.getMsg(), HttpStatus.OK);
    }

    @PostMapping("/add/{memberId}")
    @ResponseBody
    public ResponseEntity<String> addFriend(@PathVariable Long memberId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = friendRequestService.getMember(memberId);

        return addFriend(principalDetails, member.getNickname());
    }

    @PostMapping("/accept")
    public String acceptFriendRequest(@RequestParam("friendRequestId") Long friendRequestId) {
        FriendRequest friendRequest = friendRequestService.getFriendRequest(friendRequestId)
                .orElseThrow(() -> new EntityNotFoundException("FriendRequest not found."));

        Member member1 = friendRequest.getToMember();
        Member member2 = friendRequest.getFromMember();

        publisher.publishEvent(new EventAfterFriendRequestAccept(this, member1, member2)); // 친구 요청 수락 시 이벤트 발행

        friendRequestService.deleteRequest(friendRequestId); // 요청 삭제

        return rq.redirectWithMsg("/usr/member/myPage", "친구수락이 완료되었습니다.");
    }

    @PostMapping("/reject")
    public String rejectFriendRequest(@RequestParam("friendRequestId") Long friendRequestId) {
        friendRequestService.deleteRequest(friendRequestId);

        return rq.historyBack("친구 요청을 거절하셨습니다.");
    }
}
