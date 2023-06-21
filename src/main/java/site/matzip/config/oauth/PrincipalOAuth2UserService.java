package site.matzip.config.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import java.util.Optional;

@Service
@Slf4j
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("getAttributes : " + oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            log.info("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
            System.out.println("oAuth2UserInfo.toString() = " + oAuth2UserInfo.toString());
            System.out.println("oAuth2UserInfo.getProviderId() = " + oAuth2UserInfo.getProviderId());
        } else {
            log.info("카카오만 지원합니다");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        assert oAuth2UserInfo != null;
        String provider = oAuth2UserInfo.getProvider();
        System.out.println("provider = " + provider);
        // google은 sub, facebook은 id
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("password");
        String email = oAuth2UserInfo.getEmail();

        Optional<Member> findMember = memberRepository.findByUsername(username);
        if (findMember.isEmpty()) {
            Member createdMember = Member.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .build();

            memberRepository.save(createdMember);

            // Authentication 객체에 저장
            return new PrincipalDetails(createdMember, oAuth2User.getAttributes());
        } else {
            log.info(username + "님은 이미 회원입니다");
        }

        return new PrincipalDetails(findMember.get(), oAuth2User.getAttributes());
    }
}
