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

@Controller
public class PageController {

    private final DataSetService dataSetService;
    private final QuestionService questionService; 
    private static final String ATTR_MODE  = "mode";
    private static final String ATTR_SCORE = "score";
    private static final String ATTR_COUNT = "answeredCount";
    private static final int MAX_QUESTIONS = 10;

    public PageController(DataSetService dataSetService, QuestionService questionService) { 
        this.dataSetService = dataSetService;
        this.questionService = questionService;
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
        return "mode"; // ← 先頭スラッシュ不要
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

        // スコア加算（YES=point/NO=0 仕様）
        int add   = questionService.getPointFor(questionId, answer);
        int score = ((Integer) session.getAttribute(ATTR_SCORE)) + add;
        session.setAttribute(ATTR_SCORE, score);

        // 回答数カウント
        int cnt = ((Integer) session.getAttribute(ATTR_COUNT)) + 1;
        session.setAttribute(ATTR_COUNT, cnt);

        // 10問で打ち切り
        if (cnt >= MAX_QUESTIONS) {
            return finishAndShowResult(session, model, mode, score, cnt);
        }

        try {
            // ✅ 分岐して次の質問へ
            var nextQ = questionService.findNext(questionId, answer);
            model.addAttribute("question", nextQ);
            model.addAttribute("remaining", MAX_QUESTIONS - cnt);
            return "question";
        } catch (org.springframework.web.server.ResponseStatusException e) {
            // 分岐終端（nextがNULL）に到達
            return finishAndShowResult(session, model, mode, score, cnt);
        }
    }

    // 結果表示（共通）
    private String finishAndShowResult(
            HttpSession session, Model model,
            String mode, int score, int cnt) {

        // モード別メッセージ
        String modeMsg = switch (mode) {
            case "shura"  -> "修羅モード：あなたのブラック耐性は限界突破です。";
            case "iyashi" -> "癒しモード：心のオアシスが必要です。";
            case "ai"     -> "AI診断：あなたは隠れブラック体質かもしれません。";
            default       -> "通常モード：判定完了。";
        };

        // スコア帯メッセージ（DB不使用の固定レンジ版）
        String resultMsg;
        if (score == 0)                 resultMsg = "ホワイト";
        else if (score <= 100)          resultMsg = "まあ普通";
        else if (score <= 200)          resultMsg = "普通に忙しい";
        else if (score <= 300)          resultMsg = "ハラスメントする上司いるかな";
        else if (score <= 400)          resultMsg = "エブリデイ残業";
        else if (score <= 500)          resultMsg = "休日返上";
        else if (score <= 600)          resultMsg = "心も体も限界寸前";
        else if (score <= 700)          resultMsg = "もはや正気では働けない";
        else if (score <= 800)          resultMsg = "無法地帯";
        else if (score <= 900)          resultMsg = "人のやることではない";
        else                            resultMsg = "死寄りの生と死のはざま";

        // 画面表示用
        model.addAttribute("score", score);
        model.addAttribute("count", cnt);
        model.addAttribute("mode", mode);
        model.addAttribute("modeMsg", modeMsg);
        model.addAttribute("resultMsg", resultMsg);

        // 互換用の別名（もし result.html がこちらを見ているなら）
        model.addAttribute("resultMessage", resultMsg);
        model.addAttribute("finalScore", score);
        model.addAttribute("answeredCount", cnt);

        // セッションクリア（ここでATTR_*も消す）
        session.removeAttribute("currentQuestionId");
        session.removeAttribute("totalScore");
        session.removeAttribute(ATTR_MODE);
        session.removeAttribute(ATTR_SCORE);
        session.removeAttribute(ATTR_COUNT);

        return "result";
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
