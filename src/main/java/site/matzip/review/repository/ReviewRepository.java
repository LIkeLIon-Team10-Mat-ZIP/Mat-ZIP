package site.matzip.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
