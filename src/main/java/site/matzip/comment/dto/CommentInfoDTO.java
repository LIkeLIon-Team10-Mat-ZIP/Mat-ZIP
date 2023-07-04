package site.matzip.comment.dto;

import lombok.Builder;
import lombok.Data;
import site.matzip.comment.domain.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class CommentInfoDTO {
    private String profileImageUrl;
    private Long id;
    private Long authorId;
    private Long loginId;
    private String authorNickname;
    private LocalDateTime createDate;
    private String content;

    public String getFormattedCreateDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return createDate.format(formatter);
    }
}