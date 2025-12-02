package com.example.blcweb.dto;

public class ResultbgRow {
    private int minScore;
    private int maxScore;
    private String mode;    
    private String cssClass; 
    private String bgUrl;

    public ResultbgRow(int minScore, int maxScore, String mode, String cssClass, String bgUrl) {
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.mode = mode;
        this.cssClass = cssClass;
        this.bgUrl = bgUrl;
    }
    
    public int getMinScore() { return minScore; }
    public void setMinScore(int minScore) { this.minScore = minScore; }

    public int getMaxScore() { return maxScore; }
    public void setMaxScore(int maxScore) { this.maxScore = maxScore; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getCssClass() { return cssClass; }
    public void setCssClass(String cssClass) { this.cssClass = cssClass; }
    
    public String getBgUrl() { return bgUrl; }
    public void setBgUrl(String bgUrl) { this.bgUrl = bgUrl; }
    
    }
