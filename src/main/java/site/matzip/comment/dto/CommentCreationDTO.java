package site.matzip.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentCreationDTO {
    private Long authorId;
    private Long reviewId;
    @NotBlank(message = "제목을 입력해 주세요!")
    private String title;
    @NotBlank(message = "내용을 입력해 주세요!")
    private String content;

}