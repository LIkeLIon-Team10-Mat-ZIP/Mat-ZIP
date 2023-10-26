package site.matzip.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinDTO {

    private String username;
    private String password;
    private String nickname;
    private String email;
}
