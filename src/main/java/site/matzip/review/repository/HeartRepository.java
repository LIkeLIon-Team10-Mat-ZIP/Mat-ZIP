package site.matzip.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.member.domain.Member;
import site.matzip.review.domain.Heart;
import site.matzip.review.domain.Review;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    List<Heart> findByReview(Review review);

    List<Heart> findByReviewId(Long reviewId);

    Optional<Heart> findByMemberAndReview(Member member, Review review);
}
