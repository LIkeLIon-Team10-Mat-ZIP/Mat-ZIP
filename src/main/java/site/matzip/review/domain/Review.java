package site.matzip.review.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.matzip.base.domain.BaseEntity;
import site.matzip.comment.domain.Comment;
import site.matzip.matzip.domain.Matzip;
import site.matzip.member.domain.Member;
import site.matzip.reviewImage.domain.ReviewImage;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long rating;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matzip_id")
    private Matzip matzip;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id desc")
    private List<Comment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @Builder
    public Review(Matzip matzip, Member author, Long rating, String content) {
        this.matzip = matzip;
        this.author = author;
        this.rating = rating;
        this.content = content;
    }
}