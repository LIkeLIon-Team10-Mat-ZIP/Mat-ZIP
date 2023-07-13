package site.matzip.friend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendDTO {
    private Long id;
    private String profileImageUrl;
    private String friendNickname;
}
