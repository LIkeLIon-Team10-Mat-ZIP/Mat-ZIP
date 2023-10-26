package site.matzip.member.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessToken;

    private LocalDateTime accessTokenExpiredAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateToken(String accessToken, LocalDateTime accessTokenExpiredAt) {
        this.accessToken = accessToken;
        this.accessTokenExpiredAt = accessTokenExpiredAt;
    }

    @Builder
    public MemberToken(String accessToken, LocalDateTime accessTokenExpiredAt, Member member) {
        this.accessToken = accessToken;
        this.accessTokenExpiredAt = accessTokenExpiredAt;
        this.member = member;
    }
}
