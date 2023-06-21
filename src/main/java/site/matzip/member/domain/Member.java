package site.matzip.member.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@ToString
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

    @Builder
    public Member(String username, String password, String nickname ,String email) {
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
