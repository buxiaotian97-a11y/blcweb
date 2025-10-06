package com.example.blcweb.controller;

import org.springframework.web.bind.annotation.*;
import com.example.blcweb.service.QuestionService;
import com.example.blcweb.dto.QuestionDto;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService service;
    public QuestionController(QuestionService service){ this.service = service; }

    @GetMapping("/next")
    public QuestionDto next(@RequestParam(required=false) Long after){
        return service.findNext(after);
    }

    @GetMapping("/{id}")
    public QuestionDto getOne(@PathVariable Long id){
        return service.getOne(id);
    }
}
