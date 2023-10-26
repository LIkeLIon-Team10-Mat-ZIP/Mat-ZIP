package site.matzip.member.dto;

import lombok.*;

@Getter
@Builder
public class MemberInfoCntDTO {
    private int matzipCnt;
    private int reviewCnt;
    private int friendCnt;
    private long point;
}
