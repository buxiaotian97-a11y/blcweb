package com.example.blcweb.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.blcweb.dto.QuestionDto;
import com.example.blcweb.entity.Question;
import com.example.blcweb.repository.QuestionRepository;

@Service
public class QuestionService {
    private final QuestionRepository repo;
    public QuestionService(QuestionRepository repo){ this.repo = repo; }

    /** 開始質問を返す。isStart=true があれば優先、なければ最小ID */
    public QuestionDto findFirst() {
        return repo.findFirstByIsStartTrue()
                   .or(() -> repo.findFirstByOrderByIdAsc())
                   .map(QuestionDto::from)
                   .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public QuestionDto findNext(String prevCode, int answer) {
        if (prevCode == null) return findFirst();

        Question prev = repo.findByCode(prevCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (prev.isFinish()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "END");
        }

        // ★ コードで遷移
        String nextCode = (answer == 1) ? prev.getNextYesStr() : prev.getNextNoStr();
        if (nextCode == null || nextCode.isBlank()) {
            // 分岐終端（結果へ）
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "END");
        }

        Question next = repo.findByCode(nextCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (next.isFinish()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "END");
        }

        return QuestionDto.from(next);
    }


    public int getPointFor(Long questionId, int answer) {
        Question q = repo.findById(questionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return (answer == 1)
                ? q.getYesPoint()
                : q.getNoPoint();
    }

    /** 既存互換（直列進行）。使わないなら消してOK */
    public QuestionDto findNextLinear(Long after){
        return (after == null
                ? repo.findFirstByOrderByIdAsc()
                : repo.findFirstByIdGreaterThanOrderByIdAsc(after))
                .map(QuestionDto::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public QuestionDto getOne(Long id){
        return repo.findById(id)
            .map(QuestionDto::from)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    
    public int getPointForCode(String code, int answer) {
        Question q = repo.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return (answer == 1)
            ? q.getYesPoint()
            : q.getNoPoint();
    }

}
