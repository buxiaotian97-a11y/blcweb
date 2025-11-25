package com.example.blcweb.service;

public class LoginException extends RuntimeException {
    public LoginException() {
        super("ユーザー名またはパスワードが違います。");
    }
}