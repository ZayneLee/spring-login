package hello.login.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    // @GetMapping("/")
    public String home() {
        return "Home";
    }

    // @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
        if (memberId == null) {
            return "Home";
        }

        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "Home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    // @GetMapping("/")
    public String homeLoginV2(Model model, HttpServletRequest request) {

        Member member = (Member)sessionManager.getSession(request);

        if (member == null) {
            return "Home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    // @GetMapping("/")
    public String homeLoginV3(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if(session == null) {
            return "Home";
        }

        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        if (member == null) {
            return "Home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    // @GetMapping("/")
    public String homeLoginV3Spring(
        @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member, Model model) {

        if (member == null) {
            return "Home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member member, Model model) {

        if (member == null) {
            return "Home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    // @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    // @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        sessionManager.expireSession(request);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}