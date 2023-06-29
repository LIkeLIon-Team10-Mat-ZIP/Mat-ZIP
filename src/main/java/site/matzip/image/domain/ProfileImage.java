package site.matzip.image.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import site.matzip.member.domain.Member;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @NotNull
    private String imageUrl;
    private String originalImageName;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ProfileImage(String imageUrl, String originalImageName) {
        this.imageUrl = imageUrl;
        this.originalImageName = originalImageName;
    }
}
