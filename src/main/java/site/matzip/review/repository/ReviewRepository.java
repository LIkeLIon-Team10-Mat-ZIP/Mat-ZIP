package site.matzip.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.review.domain.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMatzipId(Long matzipId);
}
