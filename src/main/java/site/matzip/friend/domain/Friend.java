package site.matzip.friend.domain;

import jakarta.persistence.*;
import lombok.*;
import site.matzip.base.domain.BaseEntity;
import site.matzip.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor
public class Friend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member1_id")

    private Member member1;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member2_id")
    private Member member2;

    public void addAssociation(Member member1, Member member2) {
        addMember1(member1);
        addMember2(member2);
    }

    private void addMember1(Member member1) {
        if (this.member1 != null) {
            this.member1.getFriends1().remove(this);
        }
        this.member1 = member1;
        member1.getFriends1().add(this);
    }

    private void addMember2(Member member2) {
        if (this.member2 != null) {
            this.member2.getFriends2().remove(this);
        }
        this.member2 = member2;
        member2.getFriends2().add(this);
    }
}

