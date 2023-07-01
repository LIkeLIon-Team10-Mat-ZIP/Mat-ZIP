package site.matzip.comment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentInfoDTO {
    //private 프로필 이미지
    private String authorNickname;
    private LocalDateTime createDate;
    private String content;
}