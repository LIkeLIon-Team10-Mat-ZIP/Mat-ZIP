package site.matzip.badge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.matzip.badge.domain.Badge;
import site.matzip.badge.domain.BadgeType;
import site.matzip.badge.domain.MemberBadge;
import site.matzip.badge.repository.BadgeRepository;
import site.matzip.badge.repository.MemberBadgeRepository;
import site.matzip.matzip.domain.MatzipMember;
import site.matzip.matzip.repository.MatzipMemberRepository;
import site.matzip.member.domain.Member;
import site.matzip.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberBadgeService {
    private final BadgeRepository badgeRepository;
    private final MemberBadgeRepository memberBadgeRepository;
    private final MemberRepository memberRepository;
    private final MatzipMemberRepository matzipMemberRepository;

    @Scheduled(cron = "*/15 * * * * *")
    @Transactional
    public void matzipCountBadge() {
        List<Member> members = memberRepository.findAllWithMatzipMembers();
        Badge checkBadge = badgeRepository.findByBadgeType(BadgeType.MAP_MASTER);

        for (Member member : members) {
            checkMatzip(member, checkBadge);
        }
    }

    private void checkMatzip(Member member, Badge badge) {
        List<MatzipMember> matzipMembers = matzipMemberRepository.findByAuthor(member);
        Optional<MemberBadge> findMemberBadge =
                memberBadgeRepository.findByMemberAndBadge(member, badge);

        if (findMemberBadge.isEmpty() && matzipMembers.size() > 10) {
            MemberBadge createdMemberBadge = MemberBadge.builder().build();
            createdMemberBadge.setMember(member);
            createdMemberBadge.setBadge(badge);
            memberBadgeRepository.save(createdMemberBadge);
        }
    }
}