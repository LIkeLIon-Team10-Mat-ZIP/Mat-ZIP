package site.matzip.review.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
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