package site.matzip.member.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class MemberRankDTO {
    private String profileImageUrl;
    private String username;
    private Map<String, String> badgeImage;
    private long point;
}