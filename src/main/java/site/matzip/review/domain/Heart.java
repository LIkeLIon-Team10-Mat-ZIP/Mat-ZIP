package site.matzip.review.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import site.matzip.member.domain.Member;

@Entity
@Getter
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Builder
    public Heart() {

    }

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getHearts().remove(this);
        }
        this.member = member;
        member.getHearts().add(this);
    }

    public void setReview(Review review) {
        if (this.review != null) {
            this.review.getHearts().remove(this);
        }
        this.review = review;
        review.getHearts().add(this);
    }
}
