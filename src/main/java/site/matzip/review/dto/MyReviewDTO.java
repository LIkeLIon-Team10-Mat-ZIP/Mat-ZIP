package site.matzip.review.dto;

import lombok.Data;
import site.matzip.review.domain.Review;

import java.time.LocalDateTime;

@Data
public class MyReviewDTO {
    private String matzipName;
    private double rating;
    private String content;
    private int views;
    private LocalDateTime createDate;

    public MyReviewDTO(Review review) {
        this.matzipName = review.getMatzip().getMatzipName();
        this.rating = review.getRating();
        this.content = review.getContent();
        this.views = review.getViews();
        this.createDate = review.getCreateDate();
    }
}
