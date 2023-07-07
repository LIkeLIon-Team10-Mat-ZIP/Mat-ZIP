package site.matzip.member.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.matzip.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    Optional<Member> findByNickname(String nickname);

    @Query("SELECT m FROM Member m JOIN FETCH m.matzipMembers")
    List<Member> findAllWithMatzipMembers();

    @Query("SELECT m FROM Member m JOIN FETCH m.reviews")
    List<Member> findAllWithReviews();

    @Query("SELECT m FROM Member m JOIN FETCH m.comments")
    List<Member> findAllWithComments();

    List<Member> findTop10ByOrderByPointDesc();

    List<Member> findAllByOrderByPointDesc();

    Slice<Member> findTop4ByPointLessThanAndIdNotOrderByPointDesc(long point, long memberId, Pageable pageable);
    Slice<Member> findTop5ByPointGreaterThanAndIdNotOrderByPointAsc(long point, Long id, Pageable lower5Pageable);
}