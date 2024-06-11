package org.massmanagement.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.massmanagement.util.BeanHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AlertFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        try {
            String login = res.getHeader("login");
            if (login == null || login.isEmpty()) {
                chain.doFilter(request, response);
            }
            BeanHandler.alertService.sendAlertMessage(req);
        } catch (Exception ex) {
            chain.doFilter(request, response);
        }
    }
}
