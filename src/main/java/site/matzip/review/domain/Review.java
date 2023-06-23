package site.matzip.review.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.matzip.base.domain.BaseEntity;
import site.matzip.comment.domain.Comment;
import site.matzip.matzip.domain.Matzip;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matzip_id")
    private Matzip matzip;
    @OneToMany(mappedBy = "review", cascade = {CascadeType.ALL})
    @OrderBy("id desc")
    private List<Comment> comments = new ArrayList<>();
}