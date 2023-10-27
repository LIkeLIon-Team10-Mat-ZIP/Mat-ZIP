package site.matzip.badge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.matzip.badge.domain.*;
import site.matzip.badge.repository.*;
import site.matzip.comment.domain.Comment;
import site.matzip.comment.repository.CommentRepository;
import site.matzip.friend.repository.FriendRepository;
import site.matzip.matzip.domain.MatzipMember;
import site.matzip.matzip.repository.MatzipMemberRepository;
import site.matzip.member.domain.Member;
import site.matzip.member.repository.MemberRepository;
import site.matzip.review.domain.Review;
import site.matzip.review.repository.ReviewRepository;

import java.util.*;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberBadgeService {

    private static final int MATZIP_BADGE_COUNT = 10;
    private static final int REVIEW_BADGE_COUNT = 10;
    private static final int COMMENT_BADGE_COUNT = 10;
    private static final int FRIEND_BADGE_COUNT = 1;

    private final BadgeRepository badgeRepository;
    private final MemberBadgeRepository memberBadgeRepository;
    private final MemberRepository memberRepository;
    private final MatzipMemberRepository matzipMemberRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final FriendRepository friendRepository;

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
        List<Member> members = memberRepository.findMembersWithMatzipCountGreaterThan(MATZIP_BADGE_COUNT);
        Badge checkBadge = badgeRepository.findByBadgeType(BadgeType.MAP_MASTER);

        members.forEach(member -> checkMatzipCountCondition(member, checkBadge));
    }

    private void checkMatzipCountCondition(Member member, Badge badge) {
        List<MatzipMember> matzipMembers = matzipMemberRepository.findByAuthor(member);
        Optional<MemberBadge> findMemberBadge =
                memberBadgeRepository.findByMemberAndBadge(member, badge);

        if (findMemberBadge.isEmpty() && matzipMembers.size() > 10) {
            MemberBadge createdMemberBadge = MemberBadge.builder().build();
            createdMemberBadge.addAssociation(member, badge);
            memberBadgeRepository.save(createdMemberBadge);
        }
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void calculateReviewBadge() {
        List<Member> members = memberRepository.findMembersWithReviewsAndCountGreaterThan(REVIEW_BADGE_COUNT);
        Badge checkBadge = badgeRepository.findByBadgeType(BadgeType.COMMENTER);

        members.forEach(member -> checkReviewsCondition(member, checkBadge));
    }

    private void checkReviewsCondition(Member member, Badge badge) {
        List<Review> reviews = reviewRepository.findByAuthor(member);
        Optional<MemberBadge> findMemberBadge =
                memberBadgeRepository.findByMemberAndBadge(member, badge);

        if (findMemberBadge.isEmpty() && reviews.size() > 10) {
            MemberBadge createdMemberBadge = MemberBadge.builder().build();
            createdMemberBadge.addAssociation(member, badge);
            memberBadgeRepository.save(createdMemberBadge);
        }
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void calculateCommentBadge() {
        List<Member> members = memberRepository.findMembersWithCommentsAndCountGreaterThan(COMMENT_BADGE_COUNT);
        Badge checkBadge = badgeRepository.findByBadgeType(BadgeType.REVIEWER);

        members.forEach(member -> checkCommentsCondition(member, checkBadge));
    }

    private void checkCommentsCondition(Member member, Badge badge) {
        List<Comment> comments = commentRepository.findByAuthor(member);
        Optional<MemberBadge> findMemberBadge =
                memberBadgeRepository.findByMemberAndBadge(member, badge);

        if (findMemberBadge.isEmpty() && comments.size() > 10) {
            MemberBadge createdMemberBadge = MemberBadge.builder().build();
            createdMemberBadge.addAssociation(member, badge);
            memberBadgeRepository.save(createdMemberBadge);
        }
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void calculateHeartBadge() {
        List<Member> members = memberRepository.findAllWithReviews();
        Badge checkBadge = badgeRepository.findByBadgeType(BadgeType.LOVED_ONE);

        members.forEach(member -> checkHeartsCondition(member, checkBadge));
    }

    private void checkHeartsCondition(Member member, Badge badge) {
        List<Review> reviews = reviewRepository.findByAuthor(member);
        long totalHeartCount = reviews.stream()
                .mapToLong(review -> review.getHearts().size())
                .sum();
        Optional<MemberBadge> findMemberBadge =
                memberBadgeRepository.findByMemberAndBadge(member, badge);

        if (findMemberBadge.isEmpty() && totalHeartCount > 0) {
            MemberBadge createdMemberBadge = MemberBadge.builder().build();
            createdMemberBadge.addAssociation(member, badge);
            memberBadgeRepository.save(createdMemberBadge);
        }
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void calculateFriendBadge() {
        List<Member> members = memberRepository.findMembersWithFriends2AndCountGreaterThan(FRIEND_BADGE_COUNT);
        Badge checkBadge = badgeRepository.findByBadgeType(BadgeType.LOTS_FRIENDS);

        members.forEach(member -> checkFriendsCondition(member, checkBadge));
    }

    private void checkFriendsCondition(Member member, Badge badge) {
        Long friendsCount = friendRepository.countByMember2(member);
        Optional<MemberBadge> findMemberBadge =
                memberBadgeRepository.findByMemberAndBadge(member, badge);

        if (findMemberBadge.isEmpty() && friendsCount > 0) {
            MemberBadge createdMemberBadge = MemberBadge.builder().build();
            createdMemberBadge.addAssociation(member, badge);
            memberBadgeRepository.save(createdMemberBadge);
        }
    }
}