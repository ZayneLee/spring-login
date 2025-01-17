package hello.login.web.session;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SessionManager {
    
    private static final String SESSION_COOKIE_NAME = "mySessionId";
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    public void createSession(Object value, HttpServletResponse response) {
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(cookie);
    }

    public Object getSession(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(SESSION_COOKIE_NAME)) {
                return sessionStore.get(cookie.getValue());
            }
        }
        return null;
    }
}
