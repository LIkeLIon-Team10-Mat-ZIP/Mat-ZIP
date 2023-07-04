package site.matzip.badge.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
