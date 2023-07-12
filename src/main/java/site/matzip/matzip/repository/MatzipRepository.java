package site.matzip.matzip.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.matzip.matzip.domain.Matzip;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatzipRepository extends JpaRepository<Matzip, Long> {
    @Query("SELECT m FROM Matzip m JOIN m.matzipMembers mm WHERE mm.author.id = :authorId")
    List<Matzip> findAllByAuthorId(@Param("authorId") Long authorId);

    Optional<Matzip> findByKakaoId(Long kakaoId);

    @Query("SELECT m FROM Matzip m JOIN m.matzipMembers mm GROUP BY m ORDER BY COUNT(mm) DESC")
    List<Matzip> findTop10ByOrderByMatzipMembersSizeDesc(Pageable pageable);
}
