package site.matzip.comment.dto;

import lombok.Data;
import site.matzip.comment.domain.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class CommentInfoDTO {
    //private 프로필 이미지
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

    public CommentInfoDTO(Comment comment, Long authorId) {
        this.id = comment.getId();
        this.loginId = authorId;
        this.authorId = comment.getAuthor().getId();
        this.authorNickname = comment.getAuthor().getNickname();
        this.createDate = comment.getCreateDate();
        this.content = comment.getContent();
    }
}