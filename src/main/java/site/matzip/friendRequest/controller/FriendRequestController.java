package site.matzip.friendRequest.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.matzip.base.event.EventAfterFriendRequestAccept;
import site.matzip.base.rq.Rq;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.friendRequest.dto.FriendRequestDTO;
import site.matzip.friendRequest.entity.FriendRequest;
import site.matzip.friendRequest.service.FriendRequestService;
import site.matzip.member.domain.Member;

import java.util.List;

@Controller
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendRequestController {
    private final FriendRequestService friendRequestService;
    private final ApplicationEventPublisher publisher;
    private final Rq rq;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showList(Model model, Authentication authentication) {
        List<FriendRequestDTO> friendRequestDTOS = friendRequestService.convertToFriendRequestDTOS(rq.getMember(authentication));

        model.addAttribute("friendRequestDTOS", friendRequestDTOS);

        return "usr/friend/requestList";
    }

    @GetMapping("/add")
    public String showAddFriendForm() {
        return "usr/friend/requestForm";
    } // TODO : 추후에 삭제

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addFriend(@AuthenticationPrincipal PrincipalDetails principalDetails, String nickname) {
        Member fromMember = principalDetails.getMember();

        if (!friendRequestService.checkNicknameExists(nickname)) {
            return new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
        }

        Member toMember = friendRequestService.getMember(nickname);
        friendRequestService.addFriendRequest(toMember, fromMember);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/accept")
    public String acceptFriendRequest(@RequestParam("friendRequestId") Long friendRequestId) {
        FriendRequest friendRequest = friendRequestService.getFriendRequest(friendRequestId)
                .orElseThrow(() -> new EntityNotFoundException("FriendRequest not found."));

        Member member1 = friendRequest.getToMember();
        Member member2 = friendRequest.getFromMember();

        publisher.publishEvent(new EventAfterFriendRequestAccept(this, member1, member2)); // 친구 요청 수락 시 이벤트 발행

        friendRequestService.deleteRequest(friendRequestId); // 요청 삭제

        return "redirect:/friends/list";
    }

    @PostMapping("/reject")
    public String rejectFriendRequest(@RequestParam("friendRequestId") Long friendRequestId) {
        friendRequestService.deleteRequest(friendRequestId);

        return "redirect:/friends/list";
    }
}
