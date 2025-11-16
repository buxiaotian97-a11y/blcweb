package com.example.blcweb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.blcweb.dto.QuestionDto;
import com.example.blcweb.service.QuestionService;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService service;
    public QuestionController(QuestionService service){ this.service = service; }

    @GetMapping("/next")
    public ResponseEntity<?> next(
            @RequestParam(required = false) String after,
            @RequestParam(defaultValue = "1") int answer
    ) {
        // 0/1以外は 400
        if (answer != 0 && answer != 1) {
            return ResponseEntity.badRequest().build();
        }

        try {
            QuestionDto dto = (after == null)
                    ? service.findFirst()
                    : service.findNext(after, answer);
            return ResponseEntity.ok(dto);                 // 次の質問
        } catch (ResponseStatusException ex) {
            // Service が END を NO_CONTENT で投げる前提
            if (ex.getStatusCode() == HttpStatus.NO_CONTENT) {
                return ResponseEntity.noContent().build(); // 終了の合図（204）
            }
            throw ex; // それ以外は従来通り投げる
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDto> getOne(@PathVariable Long id){
        return ResponseEntity.ok(service.getOne(id));
    }
}
