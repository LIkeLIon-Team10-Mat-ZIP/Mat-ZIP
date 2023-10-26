package site.matzip.review.domain;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
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
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double rating;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int views;

    private boolean pointsRewarded;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matzip_id")
    @JsonIgnore
    private Matzip matzip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member author;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Heart> hearts = new ArrayList<>();

    @Builder
    public Review(double rating, String content) {
        this.rating = rating;
        this.content = content;
    }

    public void addAssociation(Matzip matzip, Member author) {
        addMatzip(matzip);
        addAuthor(author);
    }

    private void addMatzip(Matzip matzip) {
        if (this.matzip != null) {
            this.matzip.getReviews().remove(this);
        }
        this.matzip = matzip;
        matzip.getReviews().add(this);
    }

    private void addAuthor(Member author) {
        if (this.author != null) {
            this.author.getReviews().remove(this);
        }
        this.author = author;
        author.getReviews().add(this);
    }

    public void incrementViewCount() {
        this.views++;
    }

    public void updatePointsRewarded() {
        this.pointsRewarded = true;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateRating(double rating) {
        this.rating = rating;
    }

    public void removeHeart(Heart heart) {
        this.hearts.remove(heart);
    }
}