package site.matzip.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberRankDto {
    private String profileImageUrl;
    private String username;
    //private String badge;
    private long point;
}