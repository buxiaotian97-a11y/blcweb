package com.example.blcweb.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LogoutController {

  @PostMapping("/logout")
  public String logout(HttpSession session) {
    session.invalidate(); // セッション破棄（ログイン情報も消える）
    return "redirect:/title-page";
  }
}