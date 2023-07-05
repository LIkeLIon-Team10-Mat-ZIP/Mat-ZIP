package site.matzip.member.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class MemberInfoDTO {
    private String nickname;
    private String email;
    private String profileImageUrl;
    private Map<String, String> badgeImage;
}
