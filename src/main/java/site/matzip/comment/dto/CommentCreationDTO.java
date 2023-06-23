package site.matzip.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentCreationDTO {
    private Long matzipId;
    @NotBlank(message = "리뷰의 별점을 입력해 주세요!")
    private Double rating;
    @NotBlank(message = "리뷰의 제목을 입력해 주세요!")
    private String title;
    @NotBlank(message = "리뷰의 내용을 입력해 주세요!")
    private String content;

}