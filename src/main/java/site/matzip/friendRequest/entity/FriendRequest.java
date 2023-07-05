package site.matzip.friendRequest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.matzip.member.domain.Member;

@Entity
@NoArgsConstructor
@Getter
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member toMember;    // 친구 요청을 받은 멤버
    @ManyToOne
    private Member fromMember;  // 친구 요청을 보낸 멤버

    public FriendRequest(Member toMember, Member fromMember) {
        this.toMember = toMember;
        this.fromMember = fromMember;
    }
}
