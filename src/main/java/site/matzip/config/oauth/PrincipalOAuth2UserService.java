package site.matzip.config.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
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
    
    @Value("${token.content-type}")
    private String contentType;

    @Value("${token.grant_type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.registration.kakao.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String authCode = userRequest.getAccessToken().getTokenValue();
        OAuth2AccessToken accessToken = userRequest.getAccessToken();

        System.out.println("authCode.getTokenValue() = " + accessToken.getTokenValue());
        System.out.println("authCode.getExpiresAt() = " + accessToken.getExpiresAt());
        System.out.println("authCode.getScopes() = " + accessToken.getScopes());

        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            log.info("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else {
            log.info("카카오만 지원합니다");
        }

        return createOAuth2User(oAuth2User, oAuth2UserInfo, authCode);
    }

    private PrincipalDetails createOAuth2User(OAuth2User oAuth2User, OAuth2UserInfo oAuth2UserInfo, String authCode) {
       // getAccessToken(authCode);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        assert oAuth2UserInfo != null;
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("password");
        String nickname = oAuth2UserInfo.getName();
        String email = oAuth2UserInfo.getEmail();

        // OAuth2.0을 통한 회원가입 처리
        Optional<Member> findMember = memberRepository.findByUsername(username);
        if (findMember.isEmpty()) {
            return saveNewMember(oAuth2User, username, password, nickname, email);
        } else if (findMember.get().getEmail().isEmpty()) {
            return updateMember(oAuth2User, email, findMember);
        } else {
            log.info(username + "님은 이미 회원입니다");
        }

        return new PrincipalDetails(findMember.get(), oAuth2User.getAttributes());
    }

    public void unlink(String accessToken) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+accessToken);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", accessToken);

        // 해더와 바디를 하나의 오브젝트로 만들기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(null, headers);

        // Http 요청하고 리턴값을 response 변수로 받기
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/unlink", // Host
                HttpMethod.GET, // Request Method
                kakaoTokenRequest,	// RequestBody
                String.class);	// return Object

        System.out.println("response = " + response);

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+accessToken);
    }

    private void getAccessToken(String authCode) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", contentType);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", authCode);

        // 해더와 바디를 하나의 오브젝트로 만들기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        // Http 요청하고 리턴값을 response 변수로 받기
        ResponseEntity<String> response = rt.exchange(
                tokenUri, // Host
                HttpMethod.POST, // Request Method
                kakaoTokenRequest,	// RequestBody
                String.class);	// return Object

        System.out.println("response = " + response);
    }

    private PrincipalDetails saveNewMember(OAuth2User oAuth2User, String username, String password, String nickname, String email) {
        Member createdMember = Member.builder()
                .username(username)
                .password(password)
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
