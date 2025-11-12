package com.example.blcweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.blcweb.entity.ResultEntity;
import org.springframework.data.repository.query.Param;

@Repository
public interface ResultRepository extends JpaRepository<ResultEntity, Integer> {

    @Query("SELECT r.message FROM ResultEntity r WHERE :score BETWEEN r.minScore AND r.maxScore")
    String findMessageByScore(@Param("score") int score);
}