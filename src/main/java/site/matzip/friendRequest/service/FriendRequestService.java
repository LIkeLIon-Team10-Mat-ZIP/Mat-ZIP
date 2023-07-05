package site.matzip.friendRequest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.matzip.friendRequest.entity.FriendRequest;
import site.matzip.friendRequest.repository.FriendRequestRepository;
import site.matzip.member.domain.Member;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;

    public Optional<FriendRequest> getFriendRequest(Long friendRequestId) {
        return friendRequestRepository.findById(friendRequestId);
    }

    public List<FriendRequest> getFriendRequests(Member toMember) {
        return friendRequestRepository.findByToMember(toMember);
    }

    public void addFriendRequest(Member toMember, Member fromMember) {
        FriendRequest friendRequest = new FriendRequest(toMember, fromMember);

        friendRequestRepository.save(friendRequest);
    }

    public void deleteRequest(Long friendRequestId) {
        friendRequestRepository.deleteById(friendRequestId);
    }
}
