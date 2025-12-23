package com.example.blcweb.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.blcweb.entity.RecordEntity;
import com.example.blcweb.entity.UserEntity;
import com.example.blcweb.form.DataSetForm;
import com.example.blcweb.repository.ResultRepository;
import com.example.blcweb.service.AppletreeService;
import com.example.blcweb.service.BackgroundResolver;
import com.example.blcweb.service.DataSetService;
import com.example.blcweb.service.QuestionBackgroundResolver;
import com.example.blcweb.service.QuestionService;
import com.example.blcweb.service.RecordService;

@Controller
public class PageController {

    private final DataSetService dataSetService;
    private final QuestionService questionService; 
    private final ResultRepository resultRepository; 
    private final RecordService recordService;
    private final BackgroundResolver backgroundResolver;
    private final QuestionBackgroundResolver questionBackgroundResolver;
	private final AppletreeService appletreeService;
    private static final String ATTR_MODE  = "mode";
    private static final String ATTR_SCORE = "score";
    private static final String ATTR_COUNT = "answeredCount";

    public PageController(
    		DataSetService dataSetService, 
    		QuestionService questionService, 
    		ResultRepository resultRepository, 
    		RecordService recordService,
    		BackgroundResolver backgroundResolver,
    		QuestionBackgroundResolver questionBackgroundResolver,
    		AppletreeService appletreeService) { 
        this.dataSetService = dataSetService;
        this.questionService = questionService;
        this.resultRepository = resultRepository;
        this.recordService = recordService;
		this.backgroundResolver = backgroundResolver;
		this.questionBackgroundResolver = questionBackgroundResolver;
		this.appletreeService = appletreeService;
    }

    @GetMapping("/title-page")
    public String showTitle(Model model) {
        return "title";
    }

    @GetMapping("/")
    public String root() { return "redirect:/title-page"; }

 @GetMapping("/home")
 public String showHome(HttpSession session, Model model) {

     UserEntity loginUser = (UserEntity) session.getAttribute("loginUser");

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
        model.addAttribute("question", firstQ);
        
        String currentMode = (String) session.getAttribute(ATTR_MODE);
        int currentScore = (Integer) session.getAttribute(ATTR_SCORE);
        String qBgUrl = questionBackgroundResolver
                .resolveBgUrl(firstQ.getCode(), currentScore, currentMode);
        model.addAttribute("qBgUrl", qBgUrl);
        
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
            
            String qBgUrl = questionBackgroundResolver
                    .resolveBgUrl(nextQ.getCode(), score, mode);
            model.addAttribute("qBgUrl", qBgUrl);

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
            case "ai"     -> "AI";
            default       -> "standard";
        };

        String resultMsg = resultRepository.findMessageByScore(score);

     if (resultMsg == null) {
         resultMsg = "スコア範囲外";
     }
     
     boolean unlocked = (score >= 251) || (score == -200) || (score == -350);
     
     boolean specialMode = (score == -20000) || (score == -10000) || (score == -400);
     boolean showNormalButtons = !specialMode;
     
     UserEntity loginUser = (UserEntity) session.getAttribute("loginUser");
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
        String bgUrl = backgroundResolver.resolveBgUrl(score, mode);
        model.addAttribute("bgUrl", bgUrl);
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
    
    @GetMapping("/nework")
    public String showNework(Model model) {
    	model.addAttribute("nework", "転職サイト");
    	return "nework";
    }


    @GetMapping("/record")

    public String record(HttpSession session, Model model) {

        // ログインユーザー
        UserEntity loginUser = (UserEntity) session.getAttribute("loginUser");

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
    @GetMapping("/tree") public String tree() { return "tree"; }

    public record UserVM(
        String department, String name, String title,
        int days, String lastCheckedAt,
        int level, int expPercent, String character, String characterName
    ) {}
}
