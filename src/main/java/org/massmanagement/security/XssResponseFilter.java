package org.massmanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
class XssResponseFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("X-XSS-Protection","0");
        response.setHeader("X-Content-Type-Options","nosniff");
        response.setHeader("Content-Security-Policy","script-src 'self'");

        filterChain.doFilter(request,response);
    }
}
