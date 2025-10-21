package org.example.java_training.service;

import org.example.java_training.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserService {
    User save(User user);
    User findByUserName(String userName);
    boolean existsByUserName(String userName);
}
