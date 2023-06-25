package site.matzip.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import site.matzip.member.domain.Member;
import site.matzip.member.domain.MemberToken;
import site.matzip.member.repository.MemberRepository;
import site.matzip.member.repository.MemberTokenRepository;

@Service
@RequiredArgsConstructor
public class MemerService {
    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;

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

    public void logout(Long memberId) {
        MemberToken findMemberToken = findMemberToken(memberId);

        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+ findMemberToken.getAccessToken());

        // 해더와 바디를 하나의 오브젝트로 만들기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(headers);

        // Http 요청하고 리턴값을 response 변수로 받기
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/logout", // Host
                HttpMethod.POST, // Request Method
                kakaoTokenRequest,	// RequestBody
                String.class);	// return Object

        System.out.println("response = " + response);
    }

    public void unlink(Long memberId) {
        Member findMember = findMember(memberId);
        MemberToken findMemberToken = findMemberToken(memberId);

        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+ findMemberToken.getAccessToken());

        // 해더와 바디를 하나의 오브젝트로 만들기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(null, headers);

        // Http 요청하고 리턴값을 response 변수로 받기
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/unlink", // Host
                HttpMethod.POST, // Request Method
                kakaoTokenRequest,	// RequestBody
                String.class);	// return Object

        System.out.println("response = " + response);
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("찾고자 하는 Member의 ID값이 없습니다."));
    }

    private MemberToken findMemberToken(Long memberId) {
        return memberTokenRepository.findByMemberId(memberId).orElseThrow(() -> new IllegalArgumentException("찾고자 하는 MemberToken의 값이 없습니다."));
    }
}
