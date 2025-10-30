package com.example.blcweb.entity;

import jakarta.persistence.*;

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
  private int point;

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

  protected Question() {} // JPA用デフォルトコンストラクタ

  // --- getters ---
  public Long getId() { return id; }
  public String getCode() { return code; }
  public String getQtext() { return qtext; }
  public String getCategory() { return category; }
  public int getPoint() { return point; }
  public boolean isActive() { return active; }
  public Long getNextYesId() { return nextYesId; }
  public Long getNextNoId() { return nextNoId; }
  public boolean isStart() { return isStart; }

  public void setCode(String code) { this.code = code; }
  public void setQtext(String qtext) { this.qtext = qtext; }
  public void setCategory(String category) { this.category = category; }
  public void setPoint(int point) { this.point = point; }
  public void setActive(boolean active) { this.active = active; }
  public void setNextYesId(Long nextYesId) { this.nextYesId = nextYesId; }
  public void setNextNoId(Long nextNoId) { this.nextNoId = nextNoId; }
  public void setStart(boolean isStart) { this.isStart = isStart; }
}
