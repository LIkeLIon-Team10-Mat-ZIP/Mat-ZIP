package site.matzip.review.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewListDTO {
    private Long matzipId;
    private String authorNickName;
    private double rating;
    private String content;
}
