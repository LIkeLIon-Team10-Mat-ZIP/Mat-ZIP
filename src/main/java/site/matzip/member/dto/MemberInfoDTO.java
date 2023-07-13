package site.matzip.member.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class MemberInfoDTO {
    private String nickname;
    private String email;
    private String profileImageUrl;
    private Map<String, String> badgeImage;
}
