package site.matzip.matzip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.matzip.domain.MatzipRecommendation;

public interface MatzipRecommendationRepository extends JpaRepository<MatzipRecommendation,Long> {
}
