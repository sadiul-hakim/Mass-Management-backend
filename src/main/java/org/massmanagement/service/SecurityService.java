package org.massmanagement.service;

import org.massmanagement.dto.Token;
import org.massmanagement.util.JwtHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    private final CustomUserDetailsService userDetailsService;

    private static final Logger log = LoggerFactory.getLogger(SecurityService.class);

    public SecurityService(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public boolean validateToken(Token token) {

        try {
            if (token.token().isEmpty()) {
                return false;
            }

            String username = JwtHelper.extractUsername(token.token());

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            return JwtHelper.isValidToken(token.token(), userDetails);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }
}
