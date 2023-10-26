package site.matzip.config.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import site.matzip.member.domain.Member;
import site.matzip.member.repository.MemberRepository;

@Service
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public PrincipalDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("username(%s) not found".formatted(username)));

        return new PrincipalDetails(findMember);
    }
}
