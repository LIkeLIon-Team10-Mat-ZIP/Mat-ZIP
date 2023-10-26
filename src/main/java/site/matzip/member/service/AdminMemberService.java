package site.matzip.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.matzip.member.domain.Member;
import site.matzip.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Member insert() {
        if (memberRepository.existsByUsername("admin")) {
            return memberRepository.findByUsername("admin").get();
        }

        Member member = Member.builder()
                .nickname("admin")
                .username("admin")
                .password(bCryptPasswordEncoder.encode("admin1234"))
                .email("MZ_ADMIN1234@gmail.com")
                .build();
        member.setRoleAdmin();
        memberRepository.save(member);

        return member;
    }
}
