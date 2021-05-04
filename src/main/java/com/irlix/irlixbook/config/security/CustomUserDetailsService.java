package com.irlix.irlixbook.config.security;

import com.coworking.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userDetails = userService.findByEmail(email).orElseThrow(() -> {
            log.error("User not find by email " + email);
            return new UsernameNotFoundException("User not find by email " + email);});
        userDetails.getAuthorities();
        return userDetails;
        }
}
