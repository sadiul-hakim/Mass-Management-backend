package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.dto.Token;
import org.massmanagement.util.JwtHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityService {
    private final CustomUserDetailsService userDetailsService;

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
