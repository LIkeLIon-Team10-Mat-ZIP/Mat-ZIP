package site.matzip.member.dto;

import lombok.*;

import java.util.Map;

@Getter
@Builder
public class MemberInfoDTO {

    private String nickname;
    private String email;
    private String profileImageUrl;
    private Map<String, String> badgeImage;
}
