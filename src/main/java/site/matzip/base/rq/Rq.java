package site.matzip.base.rq;

import jakarta.servlet.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import site.matzip.base.rsData.RsData;
import site.matzip.base.utill.Ut;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.member.domain.Member;
import site.matzip.member.service.MemberService;

import java.util.Date;

@Component
@RequestScope
public class Rq {

    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final HttpSession session;
    private final User user;
    private final MemberService memberService;

    public Rq(HttpServletRequest req, HttpServletResponse resp, HttpSession session, MemberService memberService) {
        this.memberService = memberService;
        this.req = req;
        this.resp = resp;
        this.session = session;

        // 현재 로그인한 회원의 인증정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof User) {
            this.user = (User) authentication.getPrincipal();
        } else {
            this.user = null;
        }
    }

    // 로그인 되어 있는지 체크
    public boolean isLogin() {
        return user != null;
    }

    // 로그아웃 되어 있는지 체크
    public boolean isLogout() {
        return !isLogin();
    }

    // 뒤로가기 + 메세지
    public String historyBack(String msg) {
        String referer = req.getHeader("referer");
        String key = "historyBackErrorMsg___" + referer;
        req.setAttribute("localStorageKeyAboutHistoryBackErrorMsg", key);
        req.setAttribute("historyBackErrorMsg", msg);
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "common/js";
    }

    // 뒤로가기 + 메세지
    public String historyBack(RsData rsData) {
        return historyBack(rsData.getMsg());
    }

    // 302 + 메세지
    public String redirectWithMsg(String url, RsData rsData) {
        return redirectWithMsg(url, rsData.getMsg());
    }

    // 302 + 메세지
    public String redirectWithMsg(String url, String msg) {
        return "redirect:" + urlWithMsg(url, msg);
    }

    private String urlWithMsg(String url, String msg) {
        // 기존 URL에 혹시 msg 파라미터가 있다면 그것을 지우고 새로 넣는다.
        return Ut.url.modifyQueryParam(url, "msg", msgWithTtl(msg));
    }

    // 메세지에 ttl 적용
    private String msgWithTtl(String msg) {
        return Ut.url.encode(msg) + ";ttl=" + new Date().getTime();
    }


    public boolean isRefererAdminPage() {
        SavedRequest savedRequest = (SavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");

        if (savedRequest == null) return false;

        String referer = savedRequest.getRedirectUrl();
        return referer != null && referer.contains("/adm");
    }

    //TODO:이부분 추후에 삭제 예정
    public Member getMember(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Member author = null;

        if (principal instanceof PrincipalDetails) {
            // 주어진 PrincipalDetails 객체 사용
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            author = principalDetails.getMember();
        } else if (principal instanceof UserDetails) {
            // 주어진 Authentication 객체를 사용하고 UserDetails 중에서 member 찾기
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            author = memberService.findByUsername(username);
        }
        return author;
    }
}