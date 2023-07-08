package site.matzip.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.matzip.friend.entity.Friend;
import site.matzip.member.domain.Member;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByMember1(Member member);
}
