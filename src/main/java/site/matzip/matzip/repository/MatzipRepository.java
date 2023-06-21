package site.matzip.matzip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.matzip.matzip.domain.Matzip;

@Repository
public interface MatzipRepository extends JpaRepository<Matzip, Long> {
}
