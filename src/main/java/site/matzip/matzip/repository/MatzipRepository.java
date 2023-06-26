package site.matzip.matzip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.matzip.matzip.domain.Matzip;

import java.util.List;

@Repository
public interface MatzipRepository extends JpaRepository<Matzip, Long> {
    @Query("SELECT DISTINCT m FROM Matzip m LEFT JOIN FETCH m.recommendations")
    List<Matzip> findAllWithRecommendations();
}
