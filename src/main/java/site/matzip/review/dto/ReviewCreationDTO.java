package site.matzip.review.dto;

import lombok.Data;

@Data
public class ReviewCreationDTO {
    private Long reviewId;
    private Long rating;
    private String content;
}