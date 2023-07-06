package site.matzip.member.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class MemberProfileDTO {
    private String profileImageUrl;
    private String nickname;
    private LocalDateTime createDate;
    private long matzipCount;
    private long reviewCount;
    private long point;

    public String getFormattedCreateDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return createDate.format(formatter);
    }
}