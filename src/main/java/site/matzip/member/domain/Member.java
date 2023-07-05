package site.matzip.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.matzip.badge.domain.MemberBadge;
import site.matzip.comment.domain.Comment;
import site.matzip.image.domain.ProfileImage;
import site.matzip.matzip.domain.MatzipMember;
import site.matzip.review.domain.Review;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
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

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfileImage profileImage;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatzipMember> matzipMembers = new ArrayList<>();
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberBadge> memberBadges = new ArrayList<>();

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

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }
}