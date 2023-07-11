package site.matzip.notification.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.matzip.base.domain.BaseEntity;
import site.matzip.member.domain.Member;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@Entity
@Getter
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String typeCode;
    private LocalDateTime readDate; // 알림을 확인한 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id")
    private Member toMember; // 알림을 받는 멤버
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id")
    private Member fromMember; // 알림을 보낸 멤버

    @Builder
    public Notification(String typeCode, Member toMember, Member fromMember) {
        this.typeCode = typeCode;
        this.toMember = toMember;
        this.fromMember = fromMember;
    }

    public void setAfterReadNotification(LocalDateTime localDateTime) {
        this.readDate = localDateTime;
    }
}
