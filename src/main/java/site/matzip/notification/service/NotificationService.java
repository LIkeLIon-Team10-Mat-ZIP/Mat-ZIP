package site.matzip.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.matzip.member.domain.Member;
import site.matzip.notification.entity.Notification;
import site.matzip.notification.repository.NotificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public List<Notification> getNotifications(Member toMember) {
        return notificationRepository.findByToMember(toMember);
    }
}
