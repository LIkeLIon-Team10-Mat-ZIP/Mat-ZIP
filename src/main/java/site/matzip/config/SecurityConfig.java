package site.matzip.config;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import site.matzip.config.auth.UserLoginFailureHandler;
import site.matzip.config.oauth.PrincipalOAuth2UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private PrincipalOAuth2UserService principalOAuth2UserService;
    private final UserLoginFailureHandler userLoginFailureHandler;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/usr/member/login")
                                .failureHandler(userLoginFailureHandler)
                                .defaultSuccessUrl("/")
                )
                .oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/usr/member/login")
                                .userInfoEndpoint()
                                .userService(principalOAuth2UserService) // OAuth2.0 로그인을 성공하면 해당 service 실행
                )
                .logout(
                        logout -> logout
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
                                .addLogoutHandler((request, response, authentication) -> {
                                    // 추가적인 로그아웃 핸들링 로직
                                    Cookie[] cookies = request.getCookies();
                                    if (cookies != null) {
                                        for (Cookie cookie : cookies) {
                                            if (cookie.getName().equals("JSESSIONID")) {
                                                cookie.setMaxAge(0);
                                                response.addCookie(cookie);
                                                break;
                                            }
                                        }
                                    }
                                })
                )
        ;

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}