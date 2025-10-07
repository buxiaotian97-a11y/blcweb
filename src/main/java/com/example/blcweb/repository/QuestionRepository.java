package com.example.blcweb.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.blcweb.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
  // 最初の1件（after が無いとき用）
  Optional<Question> findTopByOrderByIdAsc();

  // 指定IDより後の先頭1件（次の質問）
  Optional<Question> findFirstByIdGreaterThanOrderByIdAsc(Long id);
}
