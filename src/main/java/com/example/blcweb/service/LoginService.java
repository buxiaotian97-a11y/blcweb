package com.example.blcweb.service;

import org.springframework.stereotype.Service;

import com.example.blcweb.entity.LoginEntity;
import com.example.blcweb.repository.LoginRepository;

@Service
public class LoginService {

    private final LoginRepository userRepository;

    public LoginService(LoginRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginEntity login(String name, String rawPassword) {

        LoginEntity user = userRepository.findByName(name)
                .orElseThrow(LoginException::new);

        if (!user.getPassword().equals(rawPassword)) {
            throw new LoginException();
        }

        return user;
    }
}
