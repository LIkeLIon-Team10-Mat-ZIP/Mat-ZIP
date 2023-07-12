package site.matzip.badge.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import site.matzip.base.domain.BaseEntity;
import site.matzip.member.domain.Member;

@Entity
@Getter
public class MemberBadge extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    private Badge badge;

    @Builder
    public MemberBadge() {}

    public void addAssociation(Member member, Badge badge) {
        if (this.member != null) {
            this.member.getMemberBadges().remove(this);
        }
        this.member = member;
        this.member.getMemberBadges().add(this);

        if (this.badge != null) {
            this.badge.getMemberBadges().remove(this);
        }
        this.badge = badge;
        this.badge.getMemberBadges().add(this);
    }
}
