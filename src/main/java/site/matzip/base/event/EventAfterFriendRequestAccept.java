package site.matzip.base.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import site.matzip.member.domain.Member;

@Getter
public class EventAfterFriendRequestAccept extends ApplicationEvent {
    private final Member member1;
    private final Member member2;

    public EventAfterFriendRequestAccept(Object source, Member member1, Member member2) {
        super(source);
        this.member1 = member1;
        this.member2 = member2;
    }
}
