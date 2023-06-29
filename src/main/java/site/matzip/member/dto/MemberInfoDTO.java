package site.matzip.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberInfoDTO {
    private String nickname;
    private String email;
}
