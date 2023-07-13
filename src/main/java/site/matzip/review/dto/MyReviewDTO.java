package site.matzip.review.dto;

import lombok.Getter;
import site.matzip.review.domain.Review;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class MyReviewDTO {
    private Long id;
    private String matzipName;
    private double rating;
    private String content;
    private int views;
    private LocalDateTime createDate;

    public MyReviewDTO(Review review) {
        this.id = review.getId();
        this.matzipName = review.getMatzip().getMatzipName();
        this.rating = review.getRating();
        this.content = review.getContent();
        this.views = review.getViews();
        this.createDate = review.getCreateDate();
    }

    public String getFormattedCreateDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return createDate.format(formatter);
    }
}
