package site.matzip.friend.dto;

import lombok.Data;
import site.matzip.badge.domain.Badge;
import site.matzip.badge.domain.MemberBadge;
import site.matzip.friend.entity.Friend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class FriendDetailDTO {
    private String profileImageUrl;
    private String friendNickname;
    private Map<String, String> badgeImage;

    public FriendDetailDTO(Friend friend) {
        this.profileImageUrl = friend.getMember2().getProfileImage().getImageUrl();
        this.friendNickname = friend.getMember2().getNickname();
        this.badgeImage =  convertMemberBadgesToMap(friend.getMember2().getMemberBadges());
    }

    private Map<String, String> convertMemberBadgesToMap(List<MemberBadge> memberBadges) {
        Map<String, String> badgeMap = new HashMap<>();

        for (MemberBadge memberBadge : memberBadges) {
            Badge badge = memberBadge.getBadge();
            String imageUrl = badge.getImageUrl();
            String badgeTypeLabel = badge.getBadgeType().label();

            badgeMap.put(imageUrl, badgeTypeLabel);
        }

        return badgeMap;
    }
}
