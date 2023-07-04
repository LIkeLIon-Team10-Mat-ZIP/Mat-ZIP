package site.matzip.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Getter
    private static String defaultProfileImageUrl;
    @Getter
    private static Long pointRewardReferenceTime;

    @Value("${custom.profileImage.defaultUrl}")
    public void setDefaultProfileImageUrl(String defaultProfileImageUrl) {
        AppConfig.defaultProfileImageUrl = defaultProfileImageUrl;
    }

    @Value("${custom.pointReward.referenceTime}")
    public void setPointRewardReferenceTime(Long pointRewardReferenceTime) {
        AppConfig.pointRewardReferenceTime = pointRewardReferenceTime;
    }

}
