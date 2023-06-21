package site.matzip.config.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        Map<String, String> kakao_account = (Map<String, String>) attributes.get("kakao_account");

        return kakao_account.get("email");
    }

    @Override
    public String getName() {
        Map<String, String> properties = (Map<String, String>) attributes.get("properties");

        return properties.get("nickname");
    }
}
