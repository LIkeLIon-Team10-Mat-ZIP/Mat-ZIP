package site.matzip.friend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.matzip.friend.entity.Friend;
import site.matzip.friend.repository.FriendRepository;
import site.matzip.member.domain.Member;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;

    @Transactional
    public void addFriend(Member member1, Member member2) {
        Friend friend1 = new Friend(member1, member2);
        friendRepository.save(friend1);

        Friend friend2 = new Friend(member2, member1);
        friendRepository.save(friend2);
    }
}
