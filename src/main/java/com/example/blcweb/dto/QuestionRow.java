package com.example.blcweb.dto;

public record QuestionRow(
    String code,
    String qtext,
    String category,
    int yesPoint,
    int noPoint,
    Long nextYesId,
    Long nextNoId,
    boolean isStart,
    boolean finish
) {}
