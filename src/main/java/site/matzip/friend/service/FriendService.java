package site.matzip.friend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.matzip.base.appConfig.AppConfig;
import site.matzip.friend.dto.FriendDTO;
import site.matzip.friend.entity.Friend;
import site.matzip.friend.repository.FriendRepository;
import site.matzip.member.domain.Member;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final AppConfig appConfig;

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

        return friendList.stream()
                .map(friend -> FriendDTO.builder()
                        .id(friend.getId())
                        .profileImageUrl(friend.getMember2().getProfileImage() != null ? friend.getMember2().getProfileImage().getImageUrl() : profileImageUrl)
                        .friendNickname(friend.getMember2().getNickname())
                        .build())
                .collect(Collectors.toList());
    }
}
