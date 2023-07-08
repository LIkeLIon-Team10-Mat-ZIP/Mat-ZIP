package site.matzip.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberProfileDTO {
    private String profileImageUrl;
    private String nickname;
    private long matzipCount;
    private long reviewCount;
    private long point;

}