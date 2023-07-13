package site.matzip.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberPointDTO {
    private long point;
    private int rank;
}
