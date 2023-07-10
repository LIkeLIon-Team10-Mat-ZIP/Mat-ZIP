package site.matzip.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberInfoCntDTO {
    private int matzipCnt;
    private int reviewCnt;
    private int friendCnt;
    private long point;
}
