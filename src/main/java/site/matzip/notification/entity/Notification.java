package site.matzip.notification.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.matzip.base.domain.BaseEntity;
import site.matzip.member.domain.Member;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String typeCode;
    private LocalDateTime readDate; // 알림을 확인한 시간

    @ManyToOne
    private Member toMember; // 알림을 받는 멤버
    @ManyToOne
    private Member fromMember; // 알림을 보낸 멤버

    public String getAfterCreateNotification() {
        long diff = ChronoUnit.SECONDS.between(getCreateDate(), LocalDateTime.now());
        if (diff < 60) return diff + "초";
        else if (diff < 3600) {
            return (diff / 60) + "분";
        } else if (diff < 86400) {
            return (diff / 60 / 60) + "시간";
        } else return (diff / 60 / 60 / 24) + "일";
    }

    public void setAfterReadNotification(LocalDateTime localDateTime) {
        this.readDate = localDateTime;
    }
}
