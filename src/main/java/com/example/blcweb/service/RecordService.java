package com.example.blcweb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.blcweb.entity.RecordEntity;
import com.example.blcweb.repository.RecordRepository;

@Service
public class RecordService {

    private final RecordRepository recordRepository;

    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    // 履歴取得
    public List<RecordEntity> getLatestRecordsForUser(Long userId) {
        return recordRepository.findTop20ByUserIdOrderByCreatedAtDesc(userId);
    }

    // 1件保存（結果画面から呼ぶ用）
    public RecordEntity save(RecordEntity recordEntity) {
        return recordRepository.save(recordEntity);
    }
}
