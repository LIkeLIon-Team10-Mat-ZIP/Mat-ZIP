package site.matzip.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.review.domain.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByMatzipId(Long matzipId, Pageable pageble);

    List<Review> findByMatzipId(Long matzipId);

}
