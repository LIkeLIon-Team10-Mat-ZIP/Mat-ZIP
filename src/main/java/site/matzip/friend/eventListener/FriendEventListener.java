package site.matzip.friend.eventListener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import site.matzip.base.event.EventAfterFriendRequestAccept;
import site.matzip.friend.service.FriendService;

@RequiredArgsConstructor
@Component
public class FriendEventListener {

    private final FriendService friendService;

    @EventListener
    public void listen(EventAfterFriendRequestAccept event) {
        friendService.whenAfterFriendRequestAccept(event.getMember1(), event.getMember2());
    }
}
