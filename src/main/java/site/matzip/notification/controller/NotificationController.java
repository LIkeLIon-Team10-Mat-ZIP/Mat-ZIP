package site.matzip.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.matzip.base.rsData.RsData;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.notification.dto.NotificationDTO;
import site.matzip.notification.service.NotificationService;

import java.util.List;

@Controller
@RequestMapping("/usr/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showList() {
        return "usr/notification/list";
    }

    @GetMapping("/reviewList")
    @PreAuthorize("isAuthenticated()")
    public String showReviewList(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getId();

        List<NotificationDTO> notificationDTOS = notificationService.convertToNotificationDTOS(memberId);

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
    public RsData deleteNotification(@RequestParam Long notificationId) {
        return notificationService.deleteNotification(notificationId);
    }

    @PostMapping("/allDelete")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public RsData allDeleteNotification(@RequestParam Integer deleteType, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return notificationService.allDeleteNotification(deleteType, principalDetails.getUserId());
    }
}
