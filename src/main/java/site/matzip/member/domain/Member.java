package site.matzip.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import site.matzip.matzip.domain.MatzipRecommendation;
import site.matzip.review.domain.Review;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)

    private final List<MatzipRecommendation> matzipRecommendations = new ArrayList<>();
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Review> reviews = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String kakao_nickname;
    private String nickname;
    private String email;
    @Enumerated(EnumType.STRING)
    private MemberRole role;
    //TODO:이 부분도 oAuth만 이용시 필요없음. 삭제예정
    private String password;


//    @Builder
//    public Member(String username, String kakao_nickname, String email) {
//        this.username = username;
//        this.kakao_nickname = kakao_nickname;
//        this.email = email;
//        role = MemberRole.ROLE_MEMBER;
//    }
    @Builder
    public Member(String username, String kakao_nickname, String nickname, String password, String email) {
        this.username = username;
        this.kakao_nickname = kakao_nickname;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        role = MemberRole.ROLE_MEMBER;
    }

    public String getEmail() {
        if (email == null) {
            return "";
        }
        return email;
    }

    public void updateEmail(String email) {
        this.email = email;
    }
}
