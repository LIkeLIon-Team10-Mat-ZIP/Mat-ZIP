package site.matzip.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.matzip.member.domain.Member;
import site.matzip.member.repository.MemberRepository;

import java.util.ArrayList;

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    // view 페이지의 input으로 받은 name="username"과 파라미터가 같아야 한다
    // SecurityConfig에서 .usernameParameter("username2");로 바꿔주어야 한다.

    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username(%s) not found".formatted(username)));
        if (member == null) {
            return null;
        }

        return new User(member.getUsername(), member.getPassword(), new ArrayList<>());
    }
}
