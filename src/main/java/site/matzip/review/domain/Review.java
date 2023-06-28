package site.matzip.review.domain;

import jakarta.persistence.*;
import lombok.*;
import site.matzip.base.domain.BaseEntity;
import site.matzip.comment.domain.Comment;
import site.matzip.image.domain.ReviewImage;
import site.matzip.matzip.domain.Matzip;
import site.matzip.member.domain.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double rating;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matzip_id")
    private Matzip matzip;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ReviewImage> reviewImages = new ArrayList<>();
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    @Builder
    public Review(Matzip matzip, Member author, Long rating, String content) {
        this.matzip = matzip;
        this.author = author;
        this.rating = rating;
        this.content = content;
    }
}