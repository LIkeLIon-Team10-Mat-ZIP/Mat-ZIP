package site.matzip.friendRequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.matzip.friendRequest.entity.FriendRequest;
import site.matzip.member.domain.Member;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByToMember(Member toMember);
}
