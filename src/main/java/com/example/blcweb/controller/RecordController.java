package com.example.blcweb.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import com.example.blcweb.entity.UserEntity;
import com.example.blcweb.entity.RecordEntity;
import com.example.blcweb.service.RecordService;

@Controller
public class RecordController {

    private final RecordService recordService;
    private final HttpSession session;

    public RecordController(RecordService recordService, HttpSession session) {
        this.recordService = recordService;
        this.session = session;
    }

    @GetMapping("/score-history")
    public String showScoreHistory(Model model) {

        // セッションのログインユーザー
        UserEntity loginUser = (UserEntity) session.getAttribute("loginUser");
        if (loginUser == null) {
            // ログインしてなかったらタイトルなどに戻す
            return "redirect:/title-page";
        }

        Long userId = loginUser.getId();
        List<RecordEntity> scores = recordService.getLatestRecordsForUser(userId);

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("scores", scores);


        return "record"; 
    }
}
