package com.example.blcweb.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.blcweb.entity.UserEntity;
import com.example.blcweb.repository.LoginRepository;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    LoginRepository loginRepository;

    @InjectMocks
    LoginService loginService;

    @Test
    void login_正しいnameとパスワードならユーザーを返す() {
        // given
        String name = "testuser";
        String rawPassword = "password";

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName(name);
        user.setPassword(rawPassword);

        when(loginRepository.findByName(name))
                .thenReturn(Optional.of(user));

        UserEntity result = loginService.login(name, rawPassword);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getPassword()).isEqualTo(rawPassword);
    }

    @Test
    void login_存在しないnameならLoginExceptionを投げる() {
        String name = "unknown";
        String rawPassword = "password";

        when(loginRepository.findByName(name))
                .thenReturn(Optional.empty());

        assertThrows(LoginException.class,
                () -> loginService.login(name, rawPassword));
    }

    @Test
    void login_パスワード不一致ならLoginExceptionを投げる() {
        String name = "testuser";
        String rawPassword = "wrong";

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName(name);
        user.setPassword("password");

        when(loginRepository.findByName(name))
                .thenReturn(Optional.of(user));

        assertThrows(LoginException.class,
                () -> loginService.login(name, rawPassword));
    }
}
