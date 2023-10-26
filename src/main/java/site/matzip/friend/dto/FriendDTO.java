package site.matzip.friend.dto;

import lombok.*;

@Getter
@Builder
public class FriendDTO {

    private Long id;
    private String profileImageUrl;
    private String friendNickname;
}
