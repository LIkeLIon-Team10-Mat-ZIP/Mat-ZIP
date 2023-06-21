package site.matzip.article.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import site.matzip.base.domain.BaseEntity;
import site.matzip.comment.domain.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "article", cascade = {CascadeType.ALL})
    @OrderBy("id desc")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

}