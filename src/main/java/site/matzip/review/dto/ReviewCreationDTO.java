package site.matzip.review.dto;

import lombok.Data;
import site.matzip.member.domain.Member;

@Data
public class ReviewCreationDTO {
    private Member author;
    private double rating;
    private String content;
}