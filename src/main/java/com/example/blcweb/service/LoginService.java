package com.example.blcweb.service;

import org.springframework.stereotype.Service;

import com.example.blcweb.entity.UserEntity;
import com.example.blcweb.repository.LoginRepository;

@Service
public class LoginService {

    private final LoginRepository userRepository;

    public LoginService(LoginRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity login(String name, String rawPassword) {

        UserEntity user = userRepository.findByName(name)
                .orElseThrow(LoginException::new);

        if (!user.getPassword().equals(rawPassword)) {
            throw new LoginException();
        }

        return user;
    }
}
