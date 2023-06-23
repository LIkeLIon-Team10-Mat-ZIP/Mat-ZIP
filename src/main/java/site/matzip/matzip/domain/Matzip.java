package site.matzip.matzip.domain;

import jakarta.persistence.*;
import lombok.*;
import site.matzip.comment.domain.Comment;
import site.matzip.review.domain.Review;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Matzip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String matzipName;
    private String description;
    private String address;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private MatzipType matzipType;
    private LocalTime openingTime;
    private LocalTime closingTime;
    @OneToMany(mappedBy = "matzip", cascade = {CascadeType.ALL})
    private List<Comment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "matzip", cascade = {CascadeType.ALL})
    private List<Review> reviews = new ArrayList<>();

}
