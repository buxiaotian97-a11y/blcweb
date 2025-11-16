package com.example.blcweb.dto;

import com.example.blcweb.entity.Question;

public class QuestionDto {
    private Long id;
    private String code;
    private String qtext;
    private String category;
    private int yespoint;
    private int nopoint;

    public QuestionDto(Long id, String code, String qtext, String category, int yes_point, int no_point) {
        this.id = id;
        this.code = code;
        this.qtext = qtext;
        this.category = category;
        this.yespoint = yes_point;
        this.nopoint = no_point;
    }

    public static QuestionDto from(Question q){
        return new QuestionDto(
            q.getId(),
            q.getCode(),      
            q.getQtext(),
            q.getCategory(),
            q.getYesPoint(),
            q.getNoPoint()
        );
    }

    public Long getId(){ return id; }
    public String getCode(){ return code; }      
    public String getQtext(){ return qtext; }
    public String getCategory(){ return category; }
    public int getYesPoint(){ return yespoint; }
    public int getNoPoint(){ return nopoint; }
}
