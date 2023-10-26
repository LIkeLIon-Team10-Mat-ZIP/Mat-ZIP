package site.matzip.member.dto;

import lombok.Getter;
import site.matzip.member.domain.Member;

@Getter
public class MemberRankInfoDTO {

    private int rank;
    private Member member;

    public MemberRankInfoDTO(int rank, Member member) {
        this.rank = rank;
        this.member = member;
    }
}
