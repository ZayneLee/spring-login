package hello.login.web.session;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import hello.login.domain.member.Member;

public class SessionManagerTest {

    SessionManager manager = new SessionManager();

    @Test
    void testSession() {
        Member member = new Member();
        MockHttpServletResponse response = new MockHttpServletResponse();
        manager.createSession(member, response);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        Object session = manager.getSession(request);
        assertThat(session).isEqualTo(member);

        manager.expireSession(request);
        Object expired = manager.getSession(request);
        assertThat(expired).isNull();
    }
}
