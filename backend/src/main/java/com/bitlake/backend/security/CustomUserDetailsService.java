package com.bitlake.backend.security;

import com.bitlake.backend.user.User;
import com.bitlake.backend.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userService.getByUsername(username);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.username())
                .password(user.hashedPassword())
                .roles("USER")
                .build();
    }
}
