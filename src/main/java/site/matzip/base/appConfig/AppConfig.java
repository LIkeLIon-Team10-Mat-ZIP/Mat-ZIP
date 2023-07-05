package site.matzip.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {
    @Value("${custom.profileImage.defaultUrl}")
    private String defaultProfileImageUrl;
    @Value("${custom.pointReward.referenceTime}")
    private Long pointRewardReferenceTime;
    @Value("${custom.pointReward.review}")
    private Long pointRewardReview;
    @Value("${custom.pointReward.comment}")
    private Long pointRewardComment;
}