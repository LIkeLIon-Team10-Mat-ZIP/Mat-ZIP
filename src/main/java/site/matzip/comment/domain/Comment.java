package site.matzip.comment.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import site.matzip.article.domain.Article;
import site.matzip.base.domain.BaseEntity;
import site.matzip.commentImage.domain.CommentImage;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;
    @OneToMany(mappedBy = "comment", cascade = {CascadeType.ALL})
    @Builder.Default
    private List<CommentImage> commentImages = new ArrayList<>();
    private Double rating;
    private String title;
    private String content;
}