package site.matzip.notification.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.matzip.base.rsData.RsData;
import site.matzip.member.domain.Member;
import site.matzip.member.repository.MemberRepository;
import site.matzip.notification.dto.NotificationDTO;
import site.matzip.notification.domain.Notification;
import site.matzip.notification.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    public List<Notification> getNotifications(Long toMemberId) {
        Member toMember = memberRepository.findById(toMemberId)
                .orElseThrow(() -> new EntityNotFoundException("member not found"));

        return notificationRepository.findByToMember(toMember);
    }

    public List<NotificationDTO> convertToNotificationDTOS(Long toMemberId) {
        List<Notification> notifications = getNotifications(toMemberId);

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

    @Transactional
    public RsData deleteNotification(Long notificationId) {
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        if (notification.isEmpty()) return RsData.of("F-1", "해당 알림이 존재하지 않습니다.");

        notificationRepository.delete(notification.get());
        return RsData.of("S-1", "해당 알림이 삭제되었습니다.");
    }

    @Transactional
    public RsData allDeleteNotification(Integer deleteType, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("member not found"));
        List<Notification> notificationList = notificationRepository.findByToMember(member);

        if (deleteType == 1) {    // 읽은 알림 전체 삭제
            if (notificationList.stream().noneMatch(notification -> notification.getReadDate() != null)) {
                return RsData.of("F-1", "삭제 할 알림이 존재하지 않습니다.");
            }

            notificationList.stream()
                    .filter(notification -> notification.getReadDate() != null)
                    .forEach(notificationRepository::delete);

            return RsData.of("S-1", "읽은 알림이 모두 삭제되었습니다.");
        } else {   // 알림 전체 삭제
            notificationRepository.deleteAll(notificationList);

            return RsData.of("S-2", "알림이 모두 삭제되었습니다.");
        }
    }
}
