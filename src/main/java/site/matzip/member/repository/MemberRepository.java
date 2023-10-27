package site.matzip.member.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import site.matzip.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    Optional<Member> findByNickname(String nickname);

    boolean existsByUsername(String username);

    @Query("SELECT m FROM Member m JOIN FETCH m.reviews")
    List<Member> findAllWithReviews();

    List<Member> findTop10ByOrderByPointDesc();

    List<Member> findAllByOrderByPointDesc();

    @Query("SELECT m FROM Member m JOIN FETCH m.matzipMembers matzip WHERE SIZE(matzip) >= :count")
    List<Member> findMembersWithMatzipCountGreaterThan(@Param("count") int count);

    @Query("SELECT m FROM Member m JOIN FETCH m.reviews r WHERE SIZE(m.reviews) >= :count")
    List<Member> findMembersWithReviewsAndCountGreaterThan(@Param("count") int count);

    @Query("SELECT m FROM Member m JOIN FETCH m.comments c WHERE SIZE(m.comments) >= :count")
    List<Member> findMembersWithCommentsAndCountGreaterThan(@Param("count") int count);

    @Query("SELECT m FROM Member m JOIN FETCH m.friends2 f WHERE SIZE(m.friends2) >= :count")
    List<Member> findMembersWithFriends2AndCountGreaterThan(@Param("count") int count);
}