package com.example.blcweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blcweb.entity.UserEntity;

public interface LoginRepository extends JpaRepository<UserEntity, Long> {

    // 既存ログインで使ってるかも
    Optional<UserEntity> findByNameAndPassword(String name, String password);

    // 新規登録用：重複チェック
    Optional<UserEntity> findByName(String name);
}
