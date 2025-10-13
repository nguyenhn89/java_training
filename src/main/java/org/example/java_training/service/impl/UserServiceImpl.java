package org.example.java_training.service.impl;

import org.example.java_training.domain.User;
import org.example.java_training.repository.UserRepository;
import org.example.java_training.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Nếu role chưa có prefix thì thêm vào
        if (!user.getRole().startsWith("ROLE_")) {
            user.setRole("ROLE_" + user.getRole());
        }

        return userRepository.save(user);
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }
}

