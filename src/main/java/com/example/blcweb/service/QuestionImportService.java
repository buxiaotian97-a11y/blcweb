package com.example.blcweb.service;

import java.util.List;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blcweb.dto.QuestionRow;
import com.example.blcweb.entity.Question;
import com.example.blcweb.repository.QuestionRepository;

@Service
public class QuestionImportService {

    private final SheetsService sheetsService;
    private final QuestionRepository questionRepository;

    public QuestionImportService(SheetsService sheetsService,
                                 QuestionRepository questionRepository) {
        this.sheetsService = sheetsService;
        this.questionRepository = questionRepository;
    }
    
    @PostConstruct
    public void init() {
        syncFromSheets();
    }


    @Transactional
    public void syncFromSheets() {

        // ① Sheets から全部取る
        List<QuestionRow> rows = sheetsService.readQuestions();

        // ② 一旦全部削除（必要に応じて差分更新にも変えられる）
        questionRepository.deleteAllInBatch();

        // ③ DBへ流し込み
        for (QuestionRow r : rows) {

            Question q = new Question();

            q.setCode(r.code());
            q.setQtext(r.qtext());
            q.setCategory(r.category());
            q.setYesPoint(r.yesPoint());
            q.setNoPoint(r.noPoint());
            q.setNextYesStr(r.nextYesStr());
            q.setNextNoStr(r.nextNoStr());
            q.setStart(r.isStart());
            q.setFinish(r.finish());

            questionRepository.save(q);
        }

    }
}
