package com.example.blcweb.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.blcweb.dto.QuestionDto;
import com.example.blcweb.repository.QuestionRepository;

@Service
public class QuestionService {
    private final QuestionRepository repo;
    public QuestionService(QuestionRepository repo){ this.repo = repo; }

    public QuestionDto findNext(Long after){
        return (after == null
            ? repo.findTopByOrderByIdAsc()
            : repo.findFirstByIdGreaterThanOrderByIdAsc(after))
            .map(QuestionDto::from)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public QuestionDto getOne(Long id){
        return repo.findById(id)
            .map(QuestionDto::from)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
