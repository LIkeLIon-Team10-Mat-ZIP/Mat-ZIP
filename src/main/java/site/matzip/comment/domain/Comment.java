package site.matzip.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.matzip.base.domain.BaseEntity;
import site.matzip.member.domain.Member;
import site.matzip.review.domain.Review;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;
    private String content;

    @Builder
    public Comment(Review review, Member author, String content) {
        this.review = review;
        this.author = author;
        this.content = content;
    }
}