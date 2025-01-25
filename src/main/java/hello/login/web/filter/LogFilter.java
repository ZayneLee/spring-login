package hello.login.web.filter;

import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogFilter implements Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
                HttpServletRequest Httprequest = (HttpServletRequest) request;
                String requestURI = Httprequest.getRequestURI();

                String uuid = UUID.randomUUID().toString();

                try {
                    log.info("REQUEST [{}][{}]",uuid,requestURI);
                    chain.doFilter(Httprequest, response);
                } catch (Exception e) {
                    throw e;
                } finally {
                    log.info("RESPONSE [{}][{}]",uuid,requestURI);
                }
    }
    
}
