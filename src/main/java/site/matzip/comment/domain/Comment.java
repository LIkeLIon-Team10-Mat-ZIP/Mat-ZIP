package site.matzip.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.matzip.base.domain.BaseEntity;
import site.matzip.commentImage.domain.CommentImage;
import site.matzip.review.domain.Review;

import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "comment", cascade = {CascadeType.ALL})
    private List<CommentImage> commentImages = new ArrayList<>();
    private Double rating;
    private String title;
    private String content;

    @Builder
    public Comment(Review review, Double rating, String title, String content) {
        this.review = review;
        this.rating = rating;
        this.title = title;
        this.content = content;
    }
}