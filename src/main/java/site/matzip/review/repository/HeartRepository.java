package site.matzip.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.member.domain.Member;
import site.matzip.review.domain.*;

import java.util.*;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    List<Heart> findByReview(Review review);

    List<Heart> findByReviewId(Long reviewId);

    Optional<Heart> findByMemberAndReview(Member member, Review review);
}
