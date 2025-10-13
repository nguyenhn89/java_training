package org.example.java_training.service.impl;

import org.example.java_training.domain.User;
import org.example.java_training.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName)
            throws UsernameNotFoundException {
//        User user = userRepository.findByUserName(userName)
//            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return new org.springframework.security.core.userdetails.User(
//            user.getUserName(),
//            user.getPassword(),
//            Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
//        );
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Đảm bảo role có prefix "ROLE_"
        String roleName = user.getRole().startsWith("ROLE_") ?
                user.getRole() : "ROLE_" + user.getRole();

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(roleName)))
                .build();
    }
}
