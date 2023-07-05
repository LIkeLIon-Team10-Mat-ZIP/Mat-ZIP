package site.matzip.friend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.matzip.member.domain.Member;

@Entity
@NoArgsConstructor
@Getter
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member1;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member2;

    public Friend(Member member1, Member member2) {
        this.member1 = member1;
        this.member2 = member2;
    }
}
