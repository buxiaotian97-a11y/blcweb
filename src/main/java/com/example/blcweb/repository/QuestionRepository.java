package com.example.blcweb.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.blcweb.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // 最初の1件を取得
    Optional<Question> findTopByOrderByIdAsc();

    // 次の1件を取得
    Optional<Question> findFirstByIdGreaterThanOrderByIdAsc(Long id);
}
