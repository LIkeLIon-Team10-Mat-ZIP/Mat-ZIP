package site.matzip.config.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.config.oauth.provider.KakaoUserInfo;
import site.matzip.config.oauth.provider.OAuth2UserInfo;
import site.matzip.member.domain.Member;
import site.matzip.member.repository.MemberRepository;
import site.matzip.member.repository.MemberTokenRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private MemberRepository memberRepository;
    private MemberTokenRepository memberTokenRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String authCode = userRequest.getAccessToken().getTokenValue();

        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            log.info("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else {
            log.info("카카오만 지원합니다");
        }

        return createOAuth2User(oAuth2User, oAuth2UserInfo);
    }

    private PrincipalDetails createOAuth2User(OAuth2User oAuth2User, OAuth2UserInfo oAuth2UserInfo) {
        assert oAuth2UserInfo != null;
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String nickname = oAuth2UserInfo.getName();
        String email = oAuth2UserInfo.getEmail();

        // OAuth2.0을 통한 회원가입 처리
        Optional<Member> findMember = memberRepository.findByUsername(username);
        if (findMember.isEmpty()) {
            return saveNewMember(oAuth2User, username, nickname, email);
        } else if (findMember.get().getEmail().isEmpty()) {
            return updateMember(oAuth2User, email, findMember);
        } else {
            log.info(username + "님은 이미 회원입니다");
        }

        return new PrincipalDetails(findMember.get(), oAuth2User.getAttributes());
    }

    private PrincipalDetails saveNewMember(OAuth2User oAuth2User, String username, String nickname, String email) {
        Member createdMember = Member.builder()
                .username(username)
                .nickname(nickname)
                .email(email)
                .build();

        memberRepository.save(createdMember);

        return new PrincipalDetails(createdMember, oAuth2User.getAttributes());
    }

    private PrincipalDetails updateMember(OAuth2User oAuth2User, String email, Optional<Member> findMember) {
        findMember.get().updateEmail(email);

        return new PrincipalDetails(findMember.get(), oAuth2User.getAttributes());
    }
}
