package hello.login.web.login;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
            HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member login = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login={}", login);

        if (login == null) {
            bindingResult.reject("loginFail", "아이디 또는 비번이 맞지 않습니다.");
            return "login/loginForm";
        }

        Cookie cookie = new Cookie("memberId", String.valueOf(login.getId()));
        response.addCookie(cookie);

        return "redirect:/";
    }
}
