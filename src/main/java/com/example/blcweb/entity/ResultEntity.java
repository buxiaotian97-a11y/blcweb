package com.example.blcweb.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "results")
public class ResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "min_score")
    private int minScore;

    @Column(name = "max_score")
    private int maxScore;

    private String message;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public int getMinScore() { return minScore; }
    public void setMinScore(int minScore) { this.minScore = minScore; }

    public int getMaxScore() { return maxScore; }
    public void setMaxScore(int maxScore) { this.maxScore = maxScore; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
