package site.matzip.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.matzip.base.rq.Rq;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.member.domain.Member;
import site.matzip.member.service.MemberService;
import site.matzip.notification.dto.NotificationDTO;
import site.matzip.notification.service.NotificationService;

import java.util.List;

@Controller
@RequestMapping("/usr/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final MemberService memberService;
    private final NotificationService notificationService;
    private final Rq rq;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showList(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = memberService.findByUsername("user1"); // TODO: 추후에 principalDetails.getMember()로 변경
      
        return "usr/notification/list";
    }

    @GetMapping("/reviewList")
    @PreAuthorize("isAuthenticated()")
    public String showReviewList(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = memberService.findByUsername("user1");

        List<NotificationDTO> notificationDTOS = notificationService.convertToNotificationDTOS(member);

        model.addAttribute("notificationDTOS", notificationDTOS);

        return "usr/notification/reviewList";
    }

    @PostMapping("/readNotification")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public String afterReadNotification(@RequestParam Long notificationId) {
        if (notificationService.getAfterReadNotification(notificationId)) {
            return "success";
        }
        return "fail";
    }

    @PostMapping("/deleteNotification")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public String deleteNotification(@RequestParam Long notificationId) {
        if (notificationService.deleteNotification(notificationId)) {
            return "success";
        }
        return "fail";
    }

    @PostMapping("/allDelete")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public String allDeleteNotification(@RequestParam Integer deleteType) {
        if (notificationService.allDeleteNotification(deleteType)) {
            return "success";
        }
        return "fail";
    }
}
