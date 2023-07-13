package site.matzip.friendRequest.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendRequestDTO {
    private Long id;
    private String fromMemberNickname;
}
