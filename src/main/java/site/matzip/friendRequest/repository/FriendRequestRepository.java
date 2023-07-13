package site.matzip.friendRequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.matzip.friendRequest.domain.FriendRequest;
import site.matzip.member.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByToMember(Member toMember);

    Optional<FriendRequest> findByFromMemberAndToMember(Member fromMember, Member toMember);
}
