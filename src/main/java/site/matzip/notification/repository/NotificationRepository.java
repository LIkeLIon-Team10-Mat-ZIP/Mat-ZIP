package site.matzip.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.matzip.member.domain.Member;
import site.matzip.notification.domain.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByToMember(Member toMember);

    int countByToMemberAndReadDateIsNull(Member member);
}
