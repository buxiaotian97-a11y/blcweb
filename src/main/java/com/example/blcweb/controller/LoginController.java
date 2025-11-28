package com.example.blcweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.blcweb.entity.UserEntity;
import com.example.blcweb.form.LoginForm;
import com.example.blcweb.service.LoginException;
import com.example.blcweb.service.LoginService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login"; 
    }

    @PostMapping("/login")
    public String doLogin(
            @ModelAttribute("loginForm") LoginForm form,
            HttpSession session,
            Model model) {

        try {

            UserEntity user = loginService.login(form.getName(), form.getPassword());

            session.setAttribute("loginUser", user);

            return "redirect:/home";

        } catch (LoginException e) {
            model.addAttribute("error", "ユーザー名またはパスワードが違います。");
            return "login";
        }
    }
}
