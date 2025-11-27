package com.example.blcweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blcweb.entity.RecordEntity;

public interface RecordRepository extends JpaRepository<RecordEntity, Long> {

    // ログインユーザーの最新20件
    List<RecordEntity> findTop20ByUserIdOrderByCreatedAtDesc(Long userId);
}
