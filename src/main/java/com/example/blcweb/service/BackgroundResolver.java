package com.example.blcweb.service;

import java.util.List;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.example.blcweb.dto.ResultbgRow;

@Service
public class BackgroundResolver {

    private final SheetsService sheetsService;
    private List<ResultbgRow> cache;

    public BackgroundResolver(SheetsService sheetsService) {
        this.sheetsService = sheetsService;
    }

    @PostConstruct
    public void init() {
        this.cache = sheetsService.readBackgrounds();
        System.out.println("背景設定を " + cache.size() + " 件読み込みました");
    }

    public String resolveCssClass(int score, String mode) {
        String currentMode = (mode == null || mode.isBlank()) ? "ANY" : mode;

        return cache.stream()
                .filter(row -> matchesMode(row.getMode(), currentMode))
                .filter(row -> row.getMinScore() <= score && score <= row.getMaxScore())
                .map(ResultbgRow::getCssClass)
                .findFirst()
                .orElse("bg-hell"); // 何もマッチしないときのデフォルト
    }

    private boolean matchesMode(String ruleMode, String currentMode) {
        if (ruleMode == null || ruleMode.isBlank()
                || "ANY".equalsIgnoreCase(ruleMode)) {
            return true;
        }
        return ruleMode.equalsIgnoreCase(currentMode);
    }
}
