package com.example.blcweb.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.blcweb.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findTopByOrderByIdAsc();                   // 最初の1件
    Optional<Question> findFirstByIdGreaterThanOrderByIdAsc(Long id); // 次の1件
}
