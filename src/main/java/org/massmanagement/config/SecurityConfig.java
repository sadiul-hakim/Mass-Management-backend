package org.massmanagement.config;

import lombok.RequiredArgsConstructor;
import org.massmanagement.security.CustomAuthenticationFilter;
import org.massmanagement.security.CustomAuthorizationFilter;
import org.massmanagement.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthorizationFilter customAuthorizationFilter;
    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {


        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(c->{
                    CorsConfigurationSource source = _ -> {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(List.of("http://localhost:8080"));
                        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
                        config.setAllowedHeaders(List.of("*"));

                        return config;
                    };

                    c.configurationSource(source);
                })
                .authorizeHttpRequests(auth -> auth.anyRequest().hasRole("MANAGER"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilter(new CustomAuthenticationFilter(authenticationProvider()))
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);

        return authenticationProvider;
    }
}
