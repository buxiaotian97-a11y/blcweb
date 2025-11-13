package com.example.blcweb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "questions")
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 質問を一意に識別するcode（例：Q_WORKING）
  @Column(nullable = false, unique = true, length = 64)
  private String code;

  @Column(nullable = false, length = 255)
  private String qtext;

  @Column(nullable = false, length = 50)
  private String category;

  @Column(nullable = false)
  private int yes_point;
  
  @Column(nullable = false)
  private int no_point;

  @Column(nullable = false)
  private boolean active = true;

  // YES/NO分岐の遷移先
  @Column(name = "next_yes_id")
  private Long nextYesId;

  @Column(name = "next_no_id")
  private Long nextNoId;

  // 最初の質問にしたい場合 true
  @Column(name = "is_start", nullable = false)
  private boolean isStart = false;
  
  @Column(name = "finish", nullable = false)
  private boolean finish;

  protected Question() {} // JPA用デフォルトコンストラクタ

  // --- getters ---
  public Long getId() { return id; }
  public String getCode() { return code; }
  public String getQtext() { return qtext; }
  public String getCategory() { return category; }
  public int getYesPoint() { return yes_point; }
  public int getNoPoint() { return no_point; }
  public boolean isActive() { return active; }
  public Long getNextYesId() { return nextYesId; }
  public Long getNextNoId() { return nextNoId; }
  public boolean isStart() { return isStart; }

  public void setCode(String code) { this.code = code; }
  public void setQtext(String qtext) { this.qtext = qtext; }
  public void setCategory(String category) { this.category = category; }
  public void setYesPoint(int yesPoint) { this.yes_point = yesPoint; }
  public void setNoPoint(int noPoint) { this.no_point = noPoint; }
  public void setActive(boolean active) { this.active = active; }
  public void setNextYesId(Long nextYesId) { this.nextYesId = nextYesId; }
  public void setNextNoId(Long nextNoId) { this.nextNoId = nextNoId; }
  public void setStart(boolean isStart) { this.isStart = isStart; }
  
  public boolean isFinish() { return finish; }
  public void setFinish(boolean finish) { this.finish = finish; }
}
