package site.matzip.friend.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class FriendDetailDTO {
    private Long id;
    private String profileImageUrl;
    private String friendNickname;
    private Map<String, String> badgeImage;
}
