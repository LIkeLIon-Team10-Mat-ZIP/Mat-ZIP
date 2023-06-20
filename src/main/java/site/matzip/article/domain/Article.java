package site.matzip.article.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import site.matzip.base.domain.BaseEntity;
import site.matzip.review.domain.Review;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class Article extends BaseEntity {

    @OneToMany(mappedBy = "review", cascade = {CascadeType.ALL})
    @OrderBy("id desc")
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    //private Member username;

    //private Restaurant restaurant;

    //private Marker place;

}