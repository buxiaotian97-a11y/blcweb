package com.example.blcweb.dto;

public class QuestionbgRow {
    private String type;
    private int questionId;
    private int minScore;    
    private String mode; 
    private String bgUrl;

    public QuestionbgRow(String type, int questionId, int minScore, String mode, String bgUrl) {
        this.type = type;
        this.questionId = questionId;
        this.minScore = minScore;
        this.mode = mode;
        this.bgUrl = bgUrl;
    }
    
    public int getMinScore() { return minScore; }
    public void setMinScore(int minScore) { this.minScore = minScore; }

    public int getquestionId() { return questionId; }
    public void setquestionId(int questionId) { this.questionId = questionId; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String gettype() { return type; }
    public void settype(String type) { this.type = type; }
    
    public String getBgUrl() { return bgUrl; }
    public void setBgUrl(String bgUrl) { this.bgUrl = bgUrl; }
    
    }
