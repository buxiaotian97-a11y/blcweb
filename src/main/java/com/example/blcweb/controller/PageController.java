package com.example.blcweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/title-page")
    public String showTitle(Model model) {
        // 必要なら model.addAttribute("key", value);
        return "title"; // => src/main/resources/templates/title.html
    }
}


