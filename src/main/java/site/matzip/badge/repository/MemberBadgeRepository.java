package site.matzip.badge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.badge.domain.MemberBadge;

public interface MemberBadgeRepository extends JpaRepository<MemberBadge, Long> {
}
