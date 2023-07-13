package site.matzip.member.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@Builder
public class MemberRankDTO {
    private int rank;
    private String profileImageUrl;
    private String nickname;
    private Map<String, String> badgeImage;
    private long point;
}