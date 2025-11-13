package com.example.blcweb.dto;

import com.example.blcweb.entity.Question;

public class QuestionDto {
  private Long id;
  private String qtext;
  private String category;
  private int yes_point;
  private int no_point;

  public QuestionDto(Long id, String qtext, String category, int yes_point, int no_point) {
    this.id = id; this.qtext = qtext; this.category = category; this.yes_point = yes_point; this.no_point = no_point;
  }
  public static QuestionDto from(Question q){
    return new QuestionDto(q.getId(), q.getQtext(), q.getCategory(), q.getYesPoint(), q.getNoPoint());
  }
  public Long getId(){ return id; }
  public String getQtext(){ return qtext; }
  public String getCategory(){ return category; }
  public int getYes_Point(){ return yes_point; }
  public int getNo_Point(){ return no_point; }
}
