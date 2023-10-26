package site.matzip.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.matzip.friend.domain.Friend;
import site.matzip.member.domain.Member;

import java.util.*;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Friend> findByMember1(Member member);

    Friend findByMember1AndMember2(Member member1, Member member2);

    Optional<Friend> findByMember1IdAndMember2Id(Long member1Id, Long member2Id);

    Long countByMember2(Member member);
}
