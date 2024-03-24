package org.massmanagement.security;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.service.CustomUserDetailsService;
import org.massmanagement.util.JwtHelper;
import org.massmanagement.util.ResponseUtility;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        try {
            if (request.getServletPath().equalsIgnoreCase("/login")) {
                filterChain.doFilter(request, response);
            } else {
                String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
                if (authorization != null && authorization.startsWith("Bearer ")) {

                    // Extract the token from authorization text
                    String token = authorization.substring("Bearer ".length());

                    // Extract the username
                    String username = JwtHelper.extractUsername(token);

                    // Get the userDetails using username
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // If the token is valid and user is not authenticated, authenticate the user
                    if (JwtHelper.isValidToken(token, userDetails) && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities() // We need to pass the Granted Authority list, otherwise user would be forbidden.
                        );
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }

                }

                // If the authorization does not exist, or it does not start with Bearer, simply let the program go.
                filterChain.doFilter(request, response);
            }
        } catch (Exception ex) {
            log.error("Error Occurred in CustomAuthorizationFilter. Cause : {}", ex.getMessage());

            // If the token is Invalid send an error with the response
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", ex.getMessage());
            ResponseUtility.commitResponse(response, errorMap,500);
        }
    }
}