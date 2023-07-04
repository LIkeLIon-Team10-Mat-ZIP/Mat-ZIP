package site.matzip.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {
    private final String defaultProfileImageUrl;
    private final Long pointRewardReferenceTime;
    private final Long pointRewardReview;
    private final Long pointRewardComment;

    public AppConfig(
            @Value("${custom.profileImage.defaultUrl}") String defaultProfileImageUrl,
            @Value("${custom.pointReward.referenceTime}") Long pointRewardReferenceTime,
            @Value("${custom.pointReward.review}") Long pointRewardReview,
            @Value("${custom.pointReward.comment}") Long pointRewardComment) {
        this.defaultProfileImageUrl = defaultProfileImageUrl;
        this.pointRewardReferenceTime = pointRewardReferenceTime;
        this.pointRewardReview = pointRewardReview;
        this.pointRewardComment = pointRewardComment;
    }
}