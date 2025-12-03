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

    /** 共通：スコア＆モードにマッチする1行を返す */
    private ResultbgRow resolveRow(int score, String mode) {
        String currentMode =
                (mode == null || mode.isBlank()) ? "ANY" : mode;

        return cache.stream()
                .filter(row -> matchesMode(row.getMode(), currentMode))
                .filter(row -> row.getMinScore() <= score && score <= row.getMaxScore())
                .findFirst()
                .orElse(null);
    }

    /** もしCSSクラスを使いたい場合用（将来用に残しておく） */
    public String resolveCssClass(int score, String mode) {
        ResultbgRow row = resolveRow(score, mode);
        if (row == null || row.getCssClass() == null || row.getCssClass().isBlank()) {
            return "bg-hell"; // マッチなしのデフォルト
        }
        return row.getCssClass();
    }

    /** 背景画像URLを返す（今回メインで使うやつ） */
    public String resolveBgUrl(int score, String mode) {
        ResultbgRow row = resolveRow(score, mode);
        if (row == null || row.getBgUrl() == null || row.getBgUrl().isBlank()) {
            return null; // → Thymeleaf側の三項演算子で style 付けない
        }
        return row.getBgUrl();
    }

    private boolean matchesMode(String ruleMode, String currentMode) {
        if (ruleMode == null || ruleMode.isBlank()
                || "ANY".equalsIgnoreCase(ruleMode)) {
            return true;
        }
        return ruleMode.equalsIgnoreCase(currentMode);
    }
}
