package site.matzip.badge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.badge.domain.Badge;
import site.matzip.badge.domain.MemberBadge;
import site.matzip.member.domain.Member;

import java.util.Optional;

public interface MemberBadgeRepository extends JpaRepository<MemberBadge, Long> {

    Optional<MemberBadge> findByMemberAndBadge(Member member, Badge badge);
}
