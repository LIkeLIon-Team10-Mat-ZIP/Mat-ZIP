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
import site.matzip.comment.domain.Comment;
import site.matzip.comment.repository.CommentRepository;
import site.matzip.matzip.domain.MatzipMember;
import site.matzip.matzip.repository.MatzipMemberRepository;
import site.matzip.member.domain.Member;
import site.matzip.member.repository.MemberRepository;
import site.matzip.review.domain.Review;
import site.matzip.review.repository.HeartRepository;
import site.matzip.review.repository.ReviewRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
// TODO count 쿼리로 변경 요소 있음
@Service
@RequiredArgsConstructor
public class MemberBadgeService {
    private final BadgeRepository badgeRepository;
    private final MemberBadgeRepository memberBadgeRepository;
    private final MemberRepository memberRepository;
    private final MatzipMemberRepository matzipMemberRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;

    public Map<String, String> showMemberBadge(Member member) {
        List<MemberBadge> memberBadges = memberBadgeRepository.findByMember(member);
        Map<String, String> badgeMap = new HashMap<>();

        for (MemberBadge memberBadge : memberBadges) {
            Badge badge = memberBadge.getBadge();
            String imageUrl = badge.getImageUrl();
            String badgeTypeLabel = badge.getBadgeType().label();

            badgeMap.put(imageUrl, badgeTypeLabel);
        }

        return badgeMap;
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void calculateMatzipCountBadge() {
        List<Member> members = memberRepository.findAllWithMatzipMembers();
        Badge checkBadge = badgeRepository.findByBadgeType(BadgeType.MAP_MASTER);

        for (Member member : members) {
            checkMatzipCount(member, checkBadge);
        }
    }

    private void checkMatzipCount(Member member, Badge badge) {
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

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void calculateReviewBadge() {
        List<Member> members = memberRepository.findAllWithReviews();
        Badge checkBadge = badgeRepository.findByBadgeType(BadgeType.COMMENTER);

        for (Member member : members) {
            checkReviews(member, checkBadge);
        }
    }

    private void checkReviews(Member member, Badge badge) {
        List<Review> reviews = reviewRepository.findByAuthor(member);
        Optional<MemberBadge> findMemberBadge =
                memberBadgeRepository.findByMemberAndBadge(member, badge);

        if (findMemberBadge.isEmpty() && reviews.size() > 10) {
            MemberBadge createdMemberBadge = MemberBadge.builder().build();
            createdMemberBadge.setMember(member);
            createdMemberBadge.setBadge(badge);
            memberBadgeRepository.save(createdMemberBadge);
        }
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void calculateCommentBadge() {
        List<Member> members = memberRepository.findAllWithComments();
        Badge checkBadge = badgeRepository.findByBadgeType(BadgeType.REVIEWER);

        for (Member member : members) {
            checkComments(member, checkBadge);
        }
    }

    private void checkComments(Member member, Badge badge) {
        List<Comment> comments = commentRepository.findByAuthor(member);
        Optional<MemberBadge> findMemberBadge =
                memberBadgeRepository.findByMemberAndBadge(member, badge);

        if (findMemberBadge.isEmpty() && comments.size() > 10) {
            MemberBadge createdMemberBadge = MemberBadge.builder().build();
            createdMemberBadge.setMember(member);
            createdMemberBadge.setBadge(badge);
            memberBadgeRepository.save(createdMemberBadge);
        }
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void calculateHeartBadge() {
        List<Member> members = memberRepository.findAllWithReviews();
        Badge checkBadge = badgeRepository.findByBadgeType(BadgeType.LOVED_ONE);

        for (Member member : members) {
            checkHearts(member, checkBadge);
        }
    }

    private void checkHearts(Member member, Badge badge) {
        List<Review> reviews = reviewRepository.findByAuthor(member);
        long totalHeartCount = reviews.stream()
                .mapToLong(review -> review.getHearts().size())
                .sum();
        Optional<MemberBadge> findMemberBadge =
                memberBadgeRepository.findByMemberAndBadge(member, badge);

        if (findMemberBadge.isEmpty() && totalHeartCount > 0) {
            MemberBadge createdMemberBadge = MemberBadge.builder().build();
            createdMemberBadge.setMember(member);
            createdMemberBadge.setBadge(badge);
            memberBadgeRepository.save(createdMemberBadge);
        }
    }
}