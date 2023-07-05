package site.matzip.badge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.badge.domain.Badge;
import site.matzip.badge.domain.BadgeType;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Badge findByBadgeType(BadgeType badgeType);
}
