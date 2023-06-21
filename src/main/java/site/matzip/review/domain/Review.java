package site.matzip.review.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import site.matzip.article.domain.Article;
import site.matzip.base.domain.BaseEntity;
import site.matzip.reviewImage.domain.ReviewImage;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Article article;
    @OneToMany(mappedBy = "review", cascade = {CascadeType.ALL})
    @Builder.Default
    private List<ReviewImage> reviewImages = new ArrayList<>();
    private Double rating;
    private String title;
    private String content;
}