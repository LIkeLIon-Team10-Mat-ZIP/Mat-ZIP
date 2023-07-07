package site.matzip.review.dto;

import lombok.Builder;
import lombok.Data;
import site.matzip.matzip.domain.MatzipType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Data
@Builder
public class ReviewDetailDTO {
    private String profileImageUrl;
    private Long reviewId;
    private int views;
    private String authorNickname;
    private LocalDateTime createDate;
    private String content;
    private String matzipName;
    private double rating;
    private MatzipType matzipType;
    private String address;
    private String phoneNumber;
    private int heartCount;
    private boolean isHeart;
    private Map<String, String> badgeImage;
    //private 리뷰이미지

    public String getFormattedCreateDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return createDate.format(formatter);
    }
}