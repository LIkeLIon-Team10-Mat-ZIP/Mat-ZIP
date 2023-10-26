package site.matzip.image.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import site.matzip.review.domain.Review;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotNull
    @Column(length = 500)
    private String imageUrl;

    private String originalImageName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Builder
    public ReviewImage(String imageUrl, String originalImageName) {
        this.imageUrl = imageUrl;
        this.originalImageName = originalImageName;
    }

    public void setReview(Review review) {
        if (this.review != null) {
            this.review.getReviewImages().remove(this);
        }
        this.review = review;
        review.getReviewImages().add(this);
    }
}