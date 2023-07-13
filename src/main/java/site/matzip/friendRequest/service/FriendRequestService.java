package site.matzip.friendRequest.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.matzip.base.rsData.RsData;
import site.matzip.friend.repository.FriendRepository;
import site.matzip.friendRequest.dto.FriendRequestDTO;
import site.matzip.friendRequest.domain.FriendRequest;
import site.matzip.friendRequest.repository.FriendRequestRepository;
import site.matzip.member.domain.Member;
import site.matzip.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    public Optional<FriendRequest> getFriendRequest(Long friendRequestId) {
        return friendRequestRepository.findById(friendRequestId);
    }

    private List<FriendRequest> getFriendRequest(Member toMember) {
        return friendRequestRepository.findByToMember(toMember);
    }

    public List<FriendRequestDTO> convertToFriendRequestDTOS(Member toMember) {
        List<FriendRequest> friendRequests = getFriendRequest(toMember);

        return friendRequests.stream()
                .map(friendRequest -> FriendRequestDTO.builder()
                        .id(friendRequest.getId())
                        .fromMemberNickname(friendRequest.getFromMember().getNickname())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void addFriendRequest(Member toMember, Member fromMember) {
        FriendRequest friendRequest = new FriendRequest(toMember, fromMember);

        friendRequestRepository.save(friendRequest);
    }

    @Transactional
    public void deleteRequest(Long friendRequestId) {
        friendRequestRepository.deleteById(friendRequestId);
    }

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("member not found"));
    }

    public Member getMember(String nickname) {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new EntityNotFoundException("member not found"));
    }

    public RsData<FriendRequest> checkRequestAdmin(String toMemberNickname, String fromMemberNickname) {
        if (memberRepository.findByNickname(toMemberNickname).isEmpty()) {
            return RsData.of("F-1", "존재하지 않는 닉네임입니다.");
        }

        if (toMemberNickname.equals(fromMemberNickname)) {
            return RsData.of("F-2", "자기 자신에게 친구 요청을 보낼 수 없습니다.");
        }

        Member toMember = memberRepository.findByNickname(toMemberNickname)
                .orElseThrow(() -> new EntityNotFoundException("member not found"));
        Member fromMember = memberRepository.findByNickname(fromMemberNickname)
                .orElseThrow(() -> new EntityNotFoundException("member not found"));

        if (friendRequestRepository.findByFromMemberAndToMember(fromMember, toMember).isPresent()) {
            return RsData.of("F-3", "이미 친구 요청을 보냈습니다.");
        }

        if (friendRequestRepository.findByFromMemberAndToMember(toMember, fromMember).isPresent()) {
            return RsData.of("F-4", "친구 요청을 수락하세요.");
        }

        if (friendRepository.findByMember1AndMember2(fromMember, toMember) != null) {
            return RsData.of("F-5", "이미 당신의 친구입니다.");
        }

        return RsData.of("S-1", "친구 요청을 보냈습니다.");
    }
}
