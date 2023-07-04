package site.matzip.badge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.badge.domain.Badge;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
}
