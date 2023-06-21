package site.matzip.commentImage.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import site.matzip.comment.domain.Comment;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;
    @NotNull
    private String imageUrl;

    @Builder
    public CommentImage(Comment comment, String imageUrl) {
        this.comment = comment;
        this.imageUrl = imageUrl;
    }
}