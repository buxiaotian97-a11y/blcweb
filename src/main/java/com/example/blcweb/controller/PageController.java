package com.example.blcweb.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.blcweb.entity.LoginEntity;
import com.example.blcweb.entity.RecordEntity;
import com.example.blcweb.form.DataSetForm;
import com.example.blcweb.repository.ResultRepository;
import com.example.blcweb.service.DataSetService;
import com.example.blcweb.service.QuestionService;
import com.example.blcweb.service.RecordService;

@Controller
public class PageController {

    private final DataSetService dataSetService;
    private final QuestionService questionService; 
    private final ResultRepository resultRepository; 
    private final RecordService recordService;
    private static final String ATTR_MODE  = "mode";
    private static final String ATTR_SCORE = "score";
    private static final String ATTR_COUNT = "answeredCount";

    public PageController(DataSetService dataSetService, QuestionService questionService, ResultRepository resultRepository, RecordService recordService) { 
        this.dataSetService = dataSetService;
        this.questionService = questionService;
        this.resultRepository = resultRepository;
        this.recordService = recordService;
    }

    @GetMapping("/title-page")
    public String showTitle(Model model) {
        return "title";
    }

    @GetMapping("/")
    public String root() { return "redirect:/title-page"; }

 @GetMapping("/home")
 public String showHome(HttpSession session, Model model) {

     LoginEntity loginUser = (LoginEntity) session.getAttribute("loginUser");

     if (loginUser == null) {
         return "redirect:/login";
     }

     var user = new UserVM(
         loginUser.getDepartmentName(),
         loginUser.getName(),
         "社畜見習い",  
         12,           
         "2025/10/01", 
         1,            
         35,           
         "salaryman",
         "サラリーマン"
     );

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
        }

        // ✅ 最初の1問（分岐対応）
        var firstQ = questionService.findFirst();
        model.addAttribute("question", firstQ);// 任意表示

        return "question";
    }

    @PostMapping("/question")
    public String answer(
            @RequestParam String questionCode,
            @RequestParam int answer,
            HttpSession session,
            Model model
    ) {
        // モード（通常 / 修羅 など）
        String mode = (String) session.getAttribute(ATTR_MODE);

        // スコア加算
        int add = questionService.getPointForCode(questionCode, answer);
        int score = ((Integer) session.getAttribute(ATTR_SCORE)) + add;
        session.setAttribute(ATTR_SCORE, score);

        // ここから先は「ツリーが続くかどうか」だけで判断
        try {
            var nextQ = questionService.findNext(questionCode, answer);
            model.addAttribute("question", nextQ);

            // もし「何問目か」表示したくなったら、ここで answeredCount をModelに乗せればOK
            return "question";
        } catch (org.springframework.web.server.ResponseStatusException e) {
            // 次の質問がない（終端） → 結果画面へ
            return finishAndShowResult(session, model, mode, score);
        }
    }


    private String finishAndShowResult(
            HttpSession session, Model model,
            String mode, int score) {

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
     
     LoginEntity loginUser = (LoginEntity) session.getAttribute("loginUser");
     if (loginUser != null) {
         RecordEntity record = new RecordEntity();
         record.setUserId(loginUser.getId());
         record.setMode(mode);
         record.setScore(score);
         
         recordService.save(record);
     }

        // 画面表示用
        model.addAttribute("score", score);
        model.addAttribute("mode", mode);
        model.addAttribute("modeMsg", modeMsg);
        model.addAttribute("resultMsg", resultMsg);
        model.addAttribute("unlocked", unlocked);
        model.addAttribute("backed", backgroundClassFor(score));
        model.addAttribute("specialMode", specialMode);
        model.addAttribute("showNormalButtons", showNormalButtons);

        model.addAttribute("resultMessage", resultMsg);
        model.addAttribute("finalScore", score);

        session.removeAttribute("currentQuestionId");
        session.removeAttribute("totalScore");
        session.removeAttribute(ATTR_MODE);
        session.removeAttribute(ATTR_SCORE);
        session.removeAttribute(ATTR_COUNT);

        return "result";
    }
    
    private String backgroundClassFor(int score) {
        if (score == -20000)  return "bg-childroomj";
        if (score == -10000)  return "bg-badneet";
        if (score == -400)  return "bg-goodneet";
        if (score == -350)  return "bg-finish";
        if (score == -300)  return "bg-careout";
        if (score == -250)  return "bg-care";
        if (score == -200)  return "bg-subworker";
        if (score == -150)  return "bg-homeworker";
        if (score == -100)  return "bg-childcare";
        if (score == -50)  return "bg-student";
        if (score >= 0 && score <= 10)  return "bg-office";
        if (score >= 11 && score <= 50)  return "bg-office2";
        if (score >= 51 && score <= 100)  return "bg-office3";
        if (score >= 101 && score <= 250)  return "bg-office4";
        if (score >= 251 && score <= 400)  return "bg-office5";
        if (score >= 401 && score <= 500)  return "bg-office6";
        if (score >= 501 && score <= 600)  return "bg-office7";
        if (score >= 601 && score <= 700)  return "bg-office8";
        if (score >= 701 && score <= 800)  return "bg-office9";
        if (score >= 801 && score <= 900)  return "bg-office10";
        if (score >= 901 && score <= 1000)  return "bg-office11";
        if (score == 1001)  return "";
        return "bg-hell";
    }
    
    @GetMapping("/nework")
    public String showNework(Model model) {
    	model.addAttribute("nework", "転職サイト");
    	return "nework";
    }


    @GetMapping("/record")
    public String records(HttpSession session, Model model) {

        // ログインユーザー
        LoginEntity loginUser = (LoginEntity) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        Long userId = loginUser.getId();

        // ログインユーザーの最新20件を取得
        var scores = recordService.getLatestRecordsForUser(userId);

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("scores", scores);

        return "record";  // ← records.html
    }

    @GetMapping("/titles")   public String titles()   { return "titles"; }
    @GetMapping("/work")     public String work()     { return "work"; }
    @GetMapping("/workflow") public String workflow() { return "workflow"; }
    @GetMapping("/hero") public String hero() { return "hero"; }

    public record UserVM(
        String department, String name, String title,
        int days, String lastCheckedAt,
        int level, int expPercent, String character, String characterName
    ) {}
}
