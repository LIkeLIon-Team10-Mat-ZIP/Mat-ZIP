package site.matzip.notification.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.matzip.member.domain.Member;
import site.matzip.member.repository.MemberRepository;
import site.matzip.notification.dto.NotificationDTO;
import site.matzip.notification.entity.Notification;
import site.matzip.notification.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

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

    @Transactional
    public boolean getAfterReadNotification(Long notificationId) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        if (notification.isEmpty()) return false;

        if (notification.get().getReadDate() == null) {
            notification.get().setAfterReadNotification(localDateTime);
        }
        return true;
    }

    public boolean countUnreadNotificationsByToMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("member not found"));
        return notificationRepository.countByToMemberAndReadDateIsNull(member) > 0;
    }

    public boolean deleteNotification(Long notificationId) {
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        if (notification.isEmpty()) return false;

        notificationRepository.delete(notification.get());
        return true;
    }

    public boolean allDeleteNotification(Integer deleteType) {
        Member member = memberRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException("member not found"));
        List<Notification> notificationList = notificationRepository.findByToMember(member);
        if (notificationList.size() == 0) return false;

        if (deleteType == 1) {    // 읽은 알림 전체 삭제
            notificationList.stream()
                    .filter(notification -> notification.getReadDate() != null)
                    .forEach(notificationRepository::delete);
        } else if (deleteType == 2) {   // 알림 전체 삭제
            notificationRepository.deleteAll(notificationList);
        }

        return true;
    }

}
