package com.example.blcweb.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 255)
  private String qtext;

  @Column(nullable = false, length = 50)
  private String category;

  @Column(nullable = false)
  private int point;

  @Column(nullable = false)
  private boolean active = true; // デフォルト有効

  protected Question() {} // JPA用デフォルトコンストラクタ

  // getters
  public Long getId() { return id; }
  public String getQtext() { return qtext; }
  public String getCategory() { return category; }
  public int getPoint() { return point; }
  public boolean isActive() { return active; }
}
