package site.matzip.review.dto;

import lombok.Data;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.domain.MatzipType;
import site.matzip.review.domain.Review;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ReviewDetailDTO {
    private String profileImageUrl;
    private Long reviewId;
    private int views;
    private String authorNickname;
    private LocalDateTime createDate;
    private String content;
    //private Long views;
    private String matzipName;
    private double rating;
    private MatzipType matzipType;
    private String address;
    private String phoneNumber;
    //private 리뷰이미지

    public ReviewDetailDTO(Review review, Matzip matzip) {
//        this.profileImageUrl = review.getAuthor().getProfileImage().getImageUrl();
        this.authorNickname = review.getAuthor().getNickname();
        //this.views =
        this.reviewId = review.getId();
        this.matzipName = matzip.getMatzipName();
        this.createDate = review.getCreateDate();
        this.address = matzip.getAddress();
        this.rating = review.getRating();
        this.views = review.getViews();
        this.matzipType = matzip.getMatzipType();
        this.phoneNumber = matzip.getPhoneNumber();
        //this.리뷰이미지
        this.content = review.getContent();
    }

    public String getFormattedCreateDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return createDate.format(formatter);
    }
}