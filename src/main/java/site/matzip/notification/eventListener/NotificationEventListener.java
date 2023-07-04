package site.matzip.notification.eventListener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import site.matzip.base.event.EventAfterComment;
import site.matzip.notification.service.NotificationService;

@RequiredArgsConstructor
@Component
public class NotificationEventListener {
    private final NotificationService notificationService;

    @EventListener
    public void listen(EventAfterComment event) {
        notificationService.whenAfterComment(event.getReviewAuthor(), event.getCommentAuthor());
    }
}