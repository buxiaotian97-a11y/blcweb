package com.example.blcweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/title-page")
    public String showTitle(Model model) {
        return "title"; // templates/title.html
    }

    @GetMapping("/")
    public String root() { return "redirect:/title-page"; }

    @GetMapping("/home")
    public String showHome(Model model) {
        model.addAttribute("user", new UserVM(
            "営業部", "佐藤 健太", "社畜見習い",
            12, "2025/10/01",
            1, 35, "salaryman", "サラリーマン"
        ));
        return "home"; // templates/home.html
    }
    
    @GetMapping("/settings")
    public String settings(Model model) {
        // ダミーのフォームデータ
        record Form(String displayName, Long departmentId, boolean notifyTitle) {}
        model.addAttribute("form", new Form("佐藤 健太", 1L, true));

        // ダミーの部署リスト
        record Dept(Long id, String name) {}
        model.addAttribute("departments", java.util.List.of(
            new Dept(1L, "営業部"),
            new Dept(2L, "開発部")
        ));

        return "settings";
    }


    @GetMapping("/records")  public String records()  { return "records";  }
    @GetMapping("/titles")   public String titles()   { return "titles";   }
    @GetMapping("/diagnose") public String diagnose() { return "diagnose"; }
    @GetMapping("/work")     public String work()     { return "work";     }

    public record UserVM(
        String department, String name, String title,
        int days, String lastCheckedAt,
        int level, int expPercent, String character, String characterName
    ) {}
}


