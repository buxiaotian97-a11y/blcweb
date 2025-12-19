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

    	 String normalizedName = (name == null) ? null : name.strip();
    	 String normalizedPassword = (rawPassword == null) ? null : rawPassword;
    	
    	 UserEntity user = userRepository.findByName(normalizedName)
    	            .orElseThrow(LoginException::new);
    	 
        if (!user.getPassword().equals(normalizedPassword)) {
            throw new LoginException();
        }

        return user;
    }
}
