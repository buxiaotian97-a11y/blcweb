package com.example.blcweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blcweb.entity.LoginEntity;

public interface LoginRepository extends JpaRepository<LoginEntity, Long> {

    // 既存ログインで使ってるかも
    Optional<LoginEntity> findByNameAndPassword(String name, String password);

    // 新規登録用：重複チェック
    Optional<LoginEntity> findByName(String name);
}
