package com.example.blcweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.blcweb.form.DataSetForm;
import com.example.blcweb.service.DataSetService;

@Controller
public class PageController {
	
	private final DataSetService dataSetService;
    public PageController(DataSetService dataSetService) {
        this.dataSetService = dataSetService;
    }

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
        if (!model.containsAttribute("form")) {
            // DataSetFormに合わせる（displayName / departmentName / notifyTitle）
            model.addAttribute("form", new DataSetForm("佐藤 健太", "営業部", true));
        }

        model.addAttribute("list", dataSetService.findAll());
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


