package site.matzip.friend.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class FriendDetailDTO {
    private Long id;
    private String profileImageUrl;
    private String friendNickname;
    private Map<String, String> badgeImage;
}
