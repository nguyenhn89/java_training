package org.example.java_training.service;

import org.example.java_training.domain.User;

public interface UserService {
    User save(User user);
    User findByUserName(String userName);
}
