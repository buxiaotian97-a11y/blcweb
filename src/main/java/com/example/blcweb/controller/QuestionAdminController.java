package com.example.blcweb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blcweb.service.QuestionImportService;

@RestController
@RequestMapping("/api/admin/questions")
public class QuestionAdminController {

    private final QuestionImportService importService;

    public QuestionAdminController(QuestionImportService importService) {
        this.importService = importService;
    }

    @PostMapping("/sync")
    public ResponseEntity<Void> sync() {
        importService.syncFromSheets();
        return ResponseEntity.ok().build();
    }
}
