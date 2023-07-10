package site.matzip.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberPointDTO {
    private long point;
    private int rank;
}
