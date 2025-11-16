package com.example.blcweb.dto;

public record QuestionRow(
    String code,
    String qtext,
    String category,
    int yesPoint,
    int noPoint,
    String nextYesStr,
    String nextNoStr,
    boolean isStart,
    boolean finish
) {}
