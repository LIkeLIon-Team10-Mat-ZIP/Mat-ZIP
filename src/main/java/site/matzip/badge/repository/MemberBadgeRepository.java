package site.matzip.badge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.matzip.badge.domain.Badge;
import site.matzip.badge.domain.MemberBadge;
import site.matzip.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberBadgeRepository extends JpaRepository<MemberBadge, Long> {

    Optional<MemberBadge> findByMemberAndBadge(Member member, Badge badge);

    @Query("SELECT m FROM MemberBadge m JOIN FETCH m.badge")
    List<MemberBadge> findByMember(Member member);
}
