package site.matzip.config.oauth;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import site.matzip.member.domain.MemberToken;
import site.matzip.member.repository.MemberRepository;
import site.matzip.member.repository.MemberTokenRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            log.info("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else {
            log.info("카카오만 지원합니다");
        }

        return createOAuth2User(userRequest, oAuth2User, oAuth2UserInfo);
    }

    // OAuth2User 생성
    private PrincipalDetails createOAuth2User(OAuth2UserRequest userRequest,
                                              OAuth2User oAuth2User,
                                              OAuth2UserInfo oAuth2UserInfo) {

        assert oAuth2UserInfo != null;
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String email = oAuth2UserInfo.getEmail();

        // OAuth2.0을 통한 회원가입 처리
        Optional<Member> findMember = memberRepository.findByUsername(username);
        if (findMember.isEmpty()) {
            return saveNewMember(userRequest, oAuth2UserInfo);
        } else if (findMember.get().getEmail().isEmpty()) {
            return updateMember(userRequest, oAuth2UserInfo, email, findMember);
        } else {
            log.info(username + "님은 이미 회원입니다");
        }

        return new PrincipalDetails(findMember.get(), oAuth2UserInfo);
    }

    // Member 새로 생성
    private PrincipalDetails saveNewMember(OAuth2UserRequest userRequest,
                                           OAuth2UserInfo oAuth2UserInfo) {

        Member createdMember = Member.builder()
                .username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                .kakao_nickname(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .build();

        saveNewMemberToken(userRequest.getAccessToken().getTokenValue(),
                userRequest.getAccessToken().getExpiresAt(),
                createdMember);
        memberRepository.save(createdMember);

        return new PrincipalDetails(createdMember, oAuth2UserInfo);
    }

    // Member 수정
    private PrincipalDetails updateMember(OAuth2UserRequest userRequest,
                                          OAuth2UserInfo oAuth2UserInfo,
                                          String email,
                                          Optional<Member> findMember) {

        findMember.get().updateEmail(email);
        saveNewMemberToken(userRequest.getAccessToken().getTokenValue(),
                userRequest.getAccessToken().getExpiresAt(),
                findMember.get());

        updateMemberToken(userRequest.getAccessToken().getTokenValue(),
                userRequest.getAccessToken().getExpiresAt(),
                findMember.get());

        return new PrincipalDetails(findMember.get(), oAuth2UserInfo);
    }

    private void saveNewMemberToken(String accessToken, Instant utcTime, Member member) {
        LocalDateTime expirationDateTime = LocalDateTime.ofInstant(utcTime, ZoneId.of("Asia/Seoul"));
        MemberToken createdMemberToken = MemberToken.builder()
                .accessToken(accessToken)
                .accessTokenExpiredAt(expirationDateTime)
                .member(member)
                .build();

        memberTokenRepository.save(createdMemberToken);
    }

    private void updateMemberToken(String accessToken, Instant utcTime, Member member) {
        LocalDateTime expirationDateTime = LocalDateTime.ofInstant(utcTime, ZoneId.of("Asia/Seoul"));
        MemberToken findMemberToken = memberTokenRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new EntityNotFoundException("MemberToken not found"));

        findMemberToken.updateToken(accessToken, expirationDateTime);
    }
}
