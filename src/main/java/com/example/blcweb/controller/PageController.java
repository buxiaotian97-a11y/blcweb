package com.example.blcweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import com.example.blcweb.form.DataSetForm;
import com.example.blcweb.service.DataSetService;
import com.example.blcweb.service.QuestionService;
import com.example.blcweb.repository.ResultRepository;

@Controller
public class PageController {

    private final DataSetService dataSetService;
    private final QuestionService questionService; 
    private final ResultRepository resultRepository; 
    private static final String ATTR_MODE  = "mode";
    private static final String ATTR_SCORE = "score";
    private static final String ATTR_COUNT = "answeredCount";
    private static final int MAX_QUESTIONS = 10;

    public PageController(DataSetService dataSetService, QuestionService questionService, ResultRepository resultRepository) { 
        this.dataSetService = dataSetService;
        this.questionService = questionService;
        this.resultRepository = resultRepository;
    }

    @GetMapping("/title-page")
    public String showTitle(Model model) {
        return "title";
    }

    @GetMapping("/")
    public String root() { return "redirect:/title-page"; }

    @GetMapping("/home")
    public String showHome(Model model) {
        var latestOpt = dataSetService.findLatest();

        var user = latestOpt
            .map(e -> new UserVM(
                e.getDepartmentName(),
                e.getName(),
                "社畜見習い",
                12,
                "2025/10/01",
                1,
                35,
                "salaryman",
                "サラリーマン"
            ))
            .orElseGet(() -> new UserVM(
                "未設定の部署",
                "未設定の名前",
                "社畜見習い",
                0,
                "—",
                1,
                0,
                "salaryman",
                "サラリーマン"
            ));

        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        if (!model.containsAttribute("form")) {
            var latestOpt = dataSetService.findLatest();
            var form = latestOpt
                .map(e -> new DataSetForm(e.getName(), e.getDepartmentName(), true))
                .orElseGet(() -> new DataSetForm("", "", true));
            model.addAttribute("form", form);
        }
        model.addAttribute("list", dataSetService.findAll());
        return "settings";
    }

    // ========== モード選択画面 ==========
    @GetMapping("/mode")
    public String showModeSelect() {
        return "mode"; 
    }

    @GetMapping("/question")
    public String showQuestion(
        @RequestParam(value = "mode", required = false) String mode,
        HttpSession session,
        Model model
    ) {
        if (mode != null && !mode.isBlank()) {
            session.setAttribute(ATTR_MODE, mode);
            System.out.println("選択モード: " + mode);
        }
        if (session.getAttribute(ATTR_MODE) == null) {
            return "redirect:/mode";
        }

        // 初回だけ初期化
        if (session.getAttribute(ATTR_SCORE) == null) {
            session.setAttribute(ATTR_SCORE, 0);
            session.setAttribute(ATTR_COUNT, 0);
            session.setAttribute("brightnessLevel", 80);
            model.addAttribute("brightnessClass", "brightness-80");
        }

        // ✅ 最初の1問（分岐対応）
        var firstQ = questionService.findFirst();
        model.addAttribute("question", firstQ);
        model.addAttribute("remaining", MAX_QUESTIONS); // 任意表示
        return "question";
    }

    @PostMapping("/question")
    public String answer(
        @RequestParam Long questionId,
        @RequestParam int answer, // YES=1 / NO=0
        HttpSession session,
        Model model
    ) {
        String mode = (String) session.getAttribute(ATTR_MODE);

        int add   = questionService.getPointFor(questionId, answer);
        int score = ((Integer) session.getAttribute(ATTR_SCORE)) + add;
        session.setAttribute(ATTR_SCORE, score);

        // 回答数カウント
        int cnt = ((Integer) session.getAttribute(ATTR_COUNT)) + 1;
        session.setAttribute(ATTR_COUNT, cnt);

        Integer brightness = (Integer) session.getAttribute("brightnessLevel");
        if (brightness == null) brightness = 100;

        if (answer == 1 && brightness > 50) {   
            brightness -= 10;
        } else if (answer == 0 && brightness < 100) { 
            brightness += 10;
        }

        session.setAttribute("brightnessLevel", brightness);
        model.addAttribute("brightnessClass", "brightness-" + brightness);
        
        // 10問で打ち切り
        if (cnt >= MAX_QUESTIONS) {
            return finishAndShowResult(session, model, mode, score, cnt);
        }

        try {
            var nextQ = questionService.findNext(questionId, answer);
            model.addAttribute("question", nextQ);
            model.addAttribute("remaining", MAX_QUESTIONS - cnt);
            return "question";
        } catch (org.springframework.web.server.ResponseStatusException e) {
            return finishAndShowResult(session, model, mode, score, cnt);
        }
    }

    private String finishAndShowResult(
            HttpSession session, Model model,
            String mode, int score, int cnt) {

        String modeMsg = switch (mode) {
            case "shura"  -> "修羅モード";
            case "iyashi" -> "癒しモード";
            case "ai"     -> "AI診断";
            default       -> "通常モード";
        };

        String resultMsg = resultRepository.findMessageByScore(score);

     if (resultMsg == null) {
         resultMsg = "スコア範囲外";
     }
     
     boolean unlocked = (score >= 251) || (score == -200) || (score == -350) || (score == -400);
     
     boolean specialMode = (score == -20000) || (score == -10000);
     boolean showNormalButtons = !specialMode;

        // 画面表示用
        model.addAttribute("score", score);
        model.addAttribute("count", cnt);
        model.addAttribute("mode", mode);
        model.addAttribute("modeMsg", modeMsg);
        model.addAttribute("resultMsg", resultMsg);
        model.addAttribute("unlocked", unlocked);
        model.addAttribute("backed", backgroundClassFor(score));
        model.addAttribute("specialMode", specialMode);
        model.addAttribute("showNormalButtons", showNormalButtons);

        model.addAttribute("resultMessage", resultMsg);
        model.addAttribute("finalScore", score);
        model.addAttribute("answeredCount", cnt);

        session.removeAttribute("currentQuestionId");
        session.removeAttribute("totalScore");
        session.removeAttribute(ATTR_MODE);
        session.removeAttribute(ATTR_SCORE);
        session.removeAttribute(ATTR_COUNT);

        return "result";
    }
    
    private String backgroundClassFor(int score) {
        if (score < 200)  return "bg-office-day";
        if (score < 400)  return "bg-office-evening";
        if (score < 600)  return "bg-office-night";
        if (score < 800)  return "bg-hell-prep";
        return "bg-hell";
    }
    
    @GetMapping("/nework")
    public String showNework(Model model) {
    	model.addAttribute("nework", "転職サイト");
    	return "nework";
    }


    @GetMapping("/records")  public String records()  { return "records"; }
    @GetMapping("/titles")   public String titles()   { return "titles"; }
    @GetMapping("/work")     public String work()     { return "work"; }

    public record UserVM(
        String department, String name, String title,
        int days, String lastCheckedAt,
        int level, int expPercent, String character, String characterName
    ) {}
}
