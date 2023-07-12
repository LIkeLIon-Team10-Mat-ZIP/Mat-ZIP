package site.matzip.review.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
public class ReviewListDTO {
    private Long matzipId;
    private Long reviewId;
    private Long authorId;
    private String authorNickname;
    private String profileImageUrl;
    private double rating;
    private String content;
    private LocalDateTime createDate;
    private long matzipCount;
    private long reviewCount;
    private long friendCount;
    private String reviewImageUrl;
    private long reviewImageCount;
}