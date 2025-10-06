package com.example.blcweb.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "answers")
public class Answer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "run_id", nullable = false, length = 36)
  private String runId;

  @Column(name = "question_id", nullable = false)
  private Long questionId;

  @Column(nullable = false)
  private boolean answer;

  @Column(name = "point_granted", nullable = false)
  private int pointGranted;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  protected Answer() {} // JPA用

  public Answer(String runId, Long questionId, boolean answer, int pointGranted) {
    this.runId = runId;
    this.questionId = questionId;
    this.answer = answer;
    this.pointGranted = pointGranted;
  }

  // getters
  public Long getId() { return id; }
  public String getRunId() { return runId; }
  public Long getQuestionId() { return questionId; }
  public boolean isAnswer() { return answer; }
  public int getPointGranted() { return pointGranted; }
  public Instant getCreatedAt() { return createdAt; }
}
