package com.example.blcweb.service;

import java.util.List;
import java.util.Optional;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import com.example.blcweb.dto.QuestionbgRow;

@Service
public class QuestionBackgroundResolver {

    private final SheetsService sheetsService;
    private List<QuestionbgRow> cache;

    public QuestionBackgroundResolver(SheetsService sheetsService) {
        this.sheetsService = sheetsService;
    }

    @PostConstruct
    public void init() {
        this.cache = sheetsService.readQuestionBackgrounds();
        System.out.println("質問背景設定を " + cache.size() + " 件読み込みました");
    }

    public String resolveBgUrl(String questionCode, int score, String mode) {
        String currentMode = (mode == null || mode.isBlank()) ? "ANY" : mode;

        Optional<QuestionbgRow> hit = cache.stream()
        		.filter(row -> java.util.Objects.equals(row.getquestionCode(), questionCode))
                .filter(row -> matchesMode(row.getMode(), currentMode))
                .filter(row -> score >= row.getMinScore()) 
                .findFirst();

        return hit.map(QuestionbgRow::getBgUrl)
                  .orElse(null);
    }

    private boolean matchesMode(String ruleMode, String currentMode) {
        if (ruleMode == null || ruleMode.isBlank()
                || "ANY".equalsIgnoreCase(ruleMode)) {
            return true;
        }
        return ruleMode.equalsIgnoreCase(currentMode);
    }
}
