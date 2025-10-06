package com.example.blcweb.dto;

import com.example.blcweb.entity.Question;

public class QuestionDto {
    private Long id;
    private String qtext;
    private String category;
    private int point;

    // コンストラクタ
    public QuestionDto(Long id, String qtext, String category, int point) {
        this.id = id;
        this.qtext = qtext;
        this.category = category;
        this.point = point;
    }

    // Entity → DTO 変換メソッド
    public static QuestionDto from(Question q) {
        return new QuestionDto(
            q.getId(),
            q.getQtext(),
            q.getCategory(),
            q.getPoint()
        );
    }

    // getterだけでOK（JacksonがJSONに変換）
    public Long getId() { return id; }
    public String getQtext() { return qtext; }
    public String getCategory() { return category; }
    public int getPoint() { return point; }
}
