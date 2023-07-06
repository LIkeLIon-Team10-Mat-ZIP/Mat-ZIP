package site.matzip.member.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import site.matzip.badge.domain.Badge;
import site.matzip.badge.domain.MemberBadge;
import site.matzip.badge.repository.MemberBadgeRepository;
import site.matzip.base.appConfig.AppConfig;
import site.matzip.base.rsData.RsData;
import site.matzip.member.domain.Member;
import site.matzip.member.domain.MemberToken;
import site.matzip.member.dto.MemberRankDTO;
import site.matzip.member.dto.NicknameUpdateDTO;
import site.matzip.member.repository.MemberRepository;
import site.matzip.member.repository.MemberTokenRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;
    private final MemberBadgeRepository memberBadgeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppConfig appConfig;

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

    public void logout(Long memberId, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        //deleteCookie(servletRequest, servletResponse);
        MemberToken findMemberToken = findMemberToken(memberId);

        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + findMemberToken.getAccessToken());

        // 해더와 바디를 하나의 오브젝트로 만들기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(headers);

        // Http 요청하고 리턴값을 response 변수로 받기
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/logout", // Host
                HttpMethod.POST, // Request Method
                kakaoTokenRequest,    // RequestBody
                String.class);    // return Object
        log.info("logout response = {}", response);
    }

    public void unlink(Long memberId) {
        Member findMember = findMember(memberId);
        MemberToken findMemberToken = findMemberToken(memberId);

        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + findMemberToken.getAccessToken());

        // 해더와 바디를 하나의 오브젝트로 만들기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(null, headers);

        // Http 요청하고 리턴값을 response 변수로 받기
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/unlink", // Host
                HttpMethod.POST, // Request Method
                kakaoTokenRequest,    // RequestBody
                String.class);    // return Object
        log.info("unlink response = {}", response);
    }

    private void deleteCookie(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        HttpSession session = servletRequest.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
        // JSESSIONID 쿠키 삭제
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        servletResponse.addCookie(cookie);
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
    }

    private MemberToken findMemberToken(Long memberId) {
        return memberTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new EntityNotFoundException("MemberToken not found"));
    }

    public Member findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
    }

    public Member signUp(String username, String kakao_nickname, String password, String email) {
        password = passwordEncoder.encode(password);
        Member member = Member.builder()
                .username(username)
                .kakao_nickname(kakao_nickname)
                .nickname(kakao_nickname)
                .password(password)
                .email(email)
                .build();
        member = memberRepository.save(member);
        return member;
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Transactional
    public RsData<Member> modifyNickname(Member member, NicknameUpdateDTO nicknameUpdateDTO) {
        if (isNicknameTaken(nicknameUpdateDTO.getNickname())) {
            return RsData.of("F-1", "이미 사용중인 닉네임 입니다.");
        }

        member.updateNickname(nicknameUpdateDTO.getNickname());

        memberRepository.save(member); // 변경사항 저장

        return RsData.of("S-1", "닉네임이 변경되었습니다.");
    }

    private boolean isNicknameTaken(String nickname) {
        Optional<Member> member = memberRepository.findByNickname(nickname);

        return member.isPresent();
    }

    public List<MemberRankDTO> findAndConvertTopTenMember() {
        List<Member> members = memberRepository.findTop10ByOrderByPointDesc();
        return members.stream().map(this::convertToMemberDTO).collect(Collectors.toList());
    }

    private MemberRankDTO convertToMemberDTO(Member member) {
        String profileImageUrl = appConfig.getDefaultProfileImageUrl();
        if (member.getProfileImage() != null && member.getProfileImage().getImageUrl() != null) {
            profileImageUrl = member.getProfileImage().getImageUrl();
        }

        List<MemberBadge> memberBadges = memberBadgeRepository.findByMember(member);
        Map<String, String> badgeMap = new HashMap<>();

        for (MemberBadge memberBadge : memberBadges) {
            Badge badge = memberBadge.getBadge();
            String imageUrl = badge.getImageUrl();
            String badgeTypeLabel = badge.getBadgeType().label();

            badgeMap.put(imageUrl, badgeTypeLabel);
        }

        return MemberRankDTO.builder()
                .profileImageUrl(profileImageUrl)
                .nickname(member.getNickname())
                .badgeImage(badgeMap)
                .point(member.getPoint())
                .build();
    }
}