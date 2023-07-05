package site.matzip.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.matzip.member.domain.Member;
import site.matzip.notification.dto.NotificationDTO;
import site.matzip.notification.entity.Notification;
import site.matzip.notification.repository.NotificationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public List<Notification> getNotifications(Member toMember) {
        return notificationRepository.findByToMember(toMember);
    }

    public List<NotificationDTO> convertToNotificationDTOS(Member toMember) {
        List<Notification> notifications = getNotifications(toMember);

        return notifications.stream()
                .map(notification -> NotificationDTO.builder()
                    .id(notification.getId())
                    .typeCode(notification.getTypeCode())
                    .createDate(notification.getCreateDate())
                    .readDate(notification.getReadDate())
                    .fromMemberNickname(notification.getFromMember().getNickname())
                    .build())
                .collect(Collectors.toList());
    }

    public void whenAfterComment(Member toMember, Member fromMember) {
        Notification notification = Notification.builder()
                .typeCode("comment")
                .toMember(toMember)     // 리뷰를 작성한 멤버
                .fromMember(fromMember) // 코멘트 작성한 멤버
                .build();

        notificationRepository.save(notification);
    }
}
