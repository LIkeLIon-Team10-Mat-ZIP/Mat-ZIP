package site.matzip.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.image.domain.ReviewImage;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
}
