package site.matzip.base.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import site.matzip.member.domain.Member;

@Getter
public class EventAfterComment extends ApplicationEvent {
    private final Member reviewAuthor;
    private final Member commentAuthor;

    public EventAfterComment(Object source, Member reviewAuthor, Member commentAuthor) {
        super(source);
        this.reviewAuthor = reviewAuthor;
        this.commentAuthor = commentAuthor;
    }
}
