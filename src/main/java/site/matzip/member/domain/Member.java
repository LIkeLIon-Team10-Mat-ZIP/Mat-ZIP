package site.matzip.member.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.matzip.matzip.domain.MatzipRecommendation;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    @Enumerated(EnumType.STRING)
    private MemberRole role;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatzipRecommendation> matzipRecommendations = new ArrayList<>();

    @Builder
    public Member(String username, String password, String nickname, String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
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
