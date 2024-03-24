package org.massmanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.massmanagement.util.JwtHelper;
import org.massmanagement.util.ResponseUtility;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationProvider authenticationProvider;

    public CustomAuthenticationFilter(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        // Extract the username and password from request attribute
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Create instance of UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // Authenticate the user
        return authenticationProvider.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        // Extract the authenticated user.
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        if(!isManager(user)){
            throw new RuntimeException("User is not a manager!");
        }

        // Generate access and refresh tokens
        // Access token has all the authority information while refresh token does not.
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", user.getAuthorities());

        String accessToken = JwtHelper.generateToken(user,extraClaims, (1000L * 60 * 60 * 24 * 7)); // expires in 7 days

        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("token",accessToken);

        ResponseUtility.commitResponse(response,tokenMap,200);
    }

    private boolean isManager(CustomUserDetails user){
        boolean isManager = false;
        for (GrantedAuthority authority : user.getAuthorities()) {
            if(authority.getAuthority().equals("ROLE_MANAGER")){
                isManager = true;
            }
        }

        return isManager;
    }
}
