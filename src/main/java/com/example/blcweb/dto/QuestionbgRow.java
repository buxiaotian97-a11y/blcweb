package com.example.blcweb.dto;

public class QuestionbgRow {
    private String type;
    private String questionCode;
    private int minScore;    
    private String mode; 
    private String bgUrl;

    public QuestionbgRow(String type, String questionCode, int minScore, String mode, String bgUrl) {
        this.type = type;
        this.questionCode = questionCode;
        this.minScore = minScore;
        this.mode = mode;
        this.bgUrl = bgUrl;
    }
    
    public int getMinScore() { return minScore; }
    public void setMinScore(int minScore) { this.minScore = minScore; }

    public String getquestionCode() { return questionCode; }
    public void setquestionCode(String questionCode) { this.questionCode = questionCode; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String gettype() { return type; }
    public void settype(String type) { this.type = type; }
    
    public String getBgUrl() { return bgUrl; }
    public void setBgUrl(String bgUrl) { this.bgUrl = bgUrl; }
    
    }
