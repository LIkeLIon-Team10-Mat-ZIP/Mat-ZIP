package site.matzip.matzip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.matzip.matzip.domain.MatzipMember;

import java.util.List;

public interface MatzipMemberRepository extends JpaRepository<MatzipMember, Long> {
    @Query("SELECT m, r FROM Matzip m " +
            "JOIN MatzipMember mm ON mm.matzip.id = m.id " +
            "LEFT JOIN Review r ON r.matzip.id = m.id AND r.author.id = :authorId " +
            "WHERE mm.author.id = :authorId")
    List<Object[]> findMyMatzipsAndReviews(Long authorId);
}
