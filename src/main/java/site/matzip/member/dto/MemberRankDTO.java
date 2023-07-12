package site.matzip.member.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@Builder
public class MemberRankDTO {
    private int rank;
    private String profileImageUrl;
    private String nickname;
    private Map<String, String> badgeImage;
    private long point;
}