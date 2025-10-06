package com.example.blcweb.controller;

import org.springframework.web.bind.annotation.*;
import com.example.blcweb.dto.AnswerRequest;
import com.example.blcweb.dto.NextQuestionResponse;
import com.example.blcweb.service.AnswerService;

@RestController
@RequestMapping("/api/answers") // ★URLはこれ
public class AnswerController {
  private final AnswerService service;
  public AnswerController(AnswerService service){ this.service = service; }

  @PostMapping
  public NextQuestionResponse submit(@RequestBody AnswerRequest req){
    return service.submitAndNext(req);
  }

  // 動作確認用
  @GetMapping("/ping")
  public String ping(){ return "pong"; }
}
