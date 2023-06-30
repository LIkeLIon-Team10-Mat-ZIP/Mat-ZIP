package site.matzip.matzip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.matzip.matzip.domain.Matzip;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatzipRepository extends JpaRepository<Matzip, Long> {
    //전체 리스트 만들 때 사용
    @Query("SELECT DISTINCT m FROM Matzip m LEFT JOIN FETCH m.matzipMemberList")
    List<Matzip> findAllWithRecommendations();

    //마이리스트 만들때 사용하자
    @Query("SELECT m FROM Matzip m JOIN m.matzipMemberList mm WHERE mm.author.id = :authorId")
    List<Matzip> findAllByAuthorId(@Param("authorId") Long authorId);

    Optional<Matzip> findByKakaoId(Long kakaoId);
}
