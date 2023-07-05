package site.matzip.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showList(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = memberService.findByUsername("user1").orElseThrow();

        List<NotificationDTO> notificationDTOS = notificationService.convertToNotificationDTOS(member);

        model.addAttribute("notificationDTOS", notificationDTOS);

        return "usr/notification/list";
    }
}
