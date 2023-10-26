package site.matzip.matzip.repository;

import org.springframework.data.jpa.repository.*;
import site.matzip.matzip.domain.MatzipMember;
import site.matzip.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MatzipMemberRepository extends JpaRepository<MatzipMember, Long> {

    @Query("SELECT m, r FROM Matzip m " +
            "JOIN MatzipMember mm ON mm.matzip.id = m.id " +
            "LEFT JOIN Review r ON r.matzip.id = m.id AND r.author.id = :authorId " +
            "WHERE mm.author.id = :authorId")
    List<Object[]> findMyMatzipsAndReviews(Long authorId);

    List<MatzipMember> findByAuthor(Member member);

    Optional<MatzipMember> findByMatzipIdAndAuthorId(Long matzipId, Long authorId);

}
