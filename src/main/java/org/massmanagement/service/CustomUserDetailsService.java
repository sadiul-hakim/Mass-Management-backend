package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import org.massmanagement.repository.UserRepo;
import org.massmanagement.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepo.findByEmail(username).orElse(null);
        if(user == null) return null;

        return new CustomUserDetails(user);
    }
}
