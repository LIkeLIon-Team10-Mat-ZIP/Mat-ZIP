package site.matzip.notification.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@Builder
public class NotificationDTO {
    private Long id;
    private String typeCode;
    private LocalDateTime createDate;
    private LocalDateTime readDate;
    private String fromMemberNickname;

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
