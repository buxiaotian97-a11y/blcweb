package com.example.blcweb.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.blcweb.entity.UserEntity;
import com.example.blcweb.form.UserRegisterForm;
import com.example.blcweb.service.UserService;

@Controller
public class SignupController {

    private final UserService userService;
    private static final String SESSION_LOGIN_USER = "loginUser"; // ← ログイン側と同じにする

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    /** 登録画面表示 */
    @GetMapping("/signup")
    public String showSignup(@ModelAttribute("userRegisterForm") UserRegisterForm form) {
        return "signup";
    }

    /** 登録処理 */
    @PostMapping("/signup")
    public String doSignup(
            @Valid @ModelAttribute("userRegisterForm") UserRegisterForm form,
            BindingResult bindingResult,
            HttpSession session,
            Model model) {

        // 1) 入力チェック
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        // 2) パスワード一致チェック
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue(
                    "confirmPassword",
                    "password.mismatch",
                    "パスワードが一致しません"
            );
            return "signup";
        }

        // 3) 名前重複チェック
        if (userService.existsByName(form.getName())) {
            bindingResult.rejectValue(
                    "name",
                    "name.duplicate",
                    "この名前はすでに使われています"
            );
            return "signup";
        }

        // 4) 登録
        UserEntity user = userService.register(form);

        // 5) そのままログイン状態にする（好みで：しないならコメントアウト）
        session.setAttribute(SESSION_LOGIN_USER, user);

        // 6) 遷移先（タイトル画面とか）
        return "redirect:/title-page";
    }
}
