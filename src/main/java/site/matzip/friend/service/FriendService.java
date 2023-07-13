package site.matzip.friend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.matzip.base.appConfig.AppConfig;
import site.matzip.friend.dto.FriendDTO;
import site.matzip.friend.domain.Friend;
import site.matzip.friend.repository.FriendRepository;
import site.matzip.member.domain.Member;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendService {
    private final FriendRepository friendRepository;
    private final AppConfig appConfig;

    public void whenAfterFriendRequestAccept(Member member1, Member member2) {
        addFriend(member1, member2); // 친구 요청 수락 시 친구 추가
    }

    @Transactional
    public void addFriend(Member member1, Member member2) {
        Friend friend1 = new Friend(member1, member2);
        friendRepository.save(friend1);

        Friend friend2 = new Friend(member2, member1);
        friendRepository.save(friend2);
    }

    public List<Friend> getFriendList(Member member) {
        return friendRepository.findByMember1(member);
    }

    public List<FriendDTO> convertToFriendDTOS(Member member) {
        List<Friend> friendList = getFriendList(member);

        String profileImageUrl = appConfig.getDefaultProfileImageUrl();

        // TODO 메서드로 분리
        return friendList.stream()
                .map(friend -> FriendDTO.builder()
                        .id(friend.getId())
                        .profileImageUrl(friend.getMember2().getProfileImage() != null ? friend.getMember2().getProfileImage().getImageUrl() : profileImageUrl)
                        .friendNickname(friend.getMember2().getNickname())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Friend friend = friendRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("friend not found"));
        Member member1 = friend.getMember1();
        Member member2 = friend.getMember2();

        Friend friend1 = friendRepository.findByMember1AndMember2(member1, member2);
        friendRepository.delete(friend1);

        Friend friend2 = friendRepository.findByMember1AndMember2(member2, member1);
        friendRepository.delete(friend2);
    }

    public boolean isFriend(Long userId, Long friendId) {
        return friendRepository.findByMember1IdAndMember2Id(userId, friendId).isPresent();
    }
}
