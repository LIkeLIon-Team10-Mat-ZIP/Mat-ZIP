package site.matzip.badge.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 500)
    private String imageUrl;

    private String originalImageName;

    // 검색키로 사용
    @Enumerated(EnumType.STRING)
    private BadgeType badgeType;

    @OneToMany(mappedBy = "badge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberBadge> memberBadges;

    @Builder
    public Badge(String imageUrl, String originalImageName, BadgeType badgeType) {
        this.imageUrl = imageUrl;
        this.originalImageName = originalImageName;
        this.badgeType = badgeType;
    }
}
