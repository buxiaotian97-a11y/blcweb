package com.example.blcweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    @GetMapping("/title-page")
    public String showTitle(Model model) {
        return "title";
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
        return "home";
    }
    
    @GetMapping("/settings")
    public String settings(Model model) {
        // ダミーのフォームデータ
        record Form(String displayName, Long departmentId, boolean notifyTitle) {}
        model.addAttribute("form", new Form("佐藤 健太", 1L, true));

        return "settings";
    }


        public record QuestionVM(Long id, String qtext) {}

        private static final int TOTAL_QUESTIONS = 10;

        // ========== モード選択画面 ==========
        @GetMapping("/mode")
        public String showModeSelect() {
            return "/mode";
        }

        // ========== 診断開始（1問目表示） ==========
        @GetMapping("/question")
        public String showQuestion(
            @RequestParam(value = "mode", required = false) String mode,
            HttpSession session,
            Model model
        ) {
            // モードが指定されていたらセッションに保存
            if (mode != null && !mode.isBlank()) {
                session.setAttribute("mode", mode);
                System.out.println("選択モード: " + mode);
            }

            // モードが未設定ならモード選択へ戻す
            if (session.getAttribute("mode") == null) {
                return "redirect:/question/mode";
            }

            // 1問目をセット
            model.addAttribute("question", new QuestionVM(1L,
                "自身の職場をブラック企業なのではないかと感じたことはありますか？"));
            return "question";
        }

        // ========== 回答処理（次の質問 or 結果） ==========
        @PostMapping("/question")
        public String answer(
            @RequestParam Long questionId,
            @RequestParam String answer,
            HttpSession session,
            Model model
        ) {
            String mode = (String) session.getAttribute("mode");
            System.out.println("Mode: " + mode + " | Q" + questionId + " -> " + answer);

            // 仮：10問目を超えたら結果画面へ
            if (questionId >= TOTAL_QUESTIONS) {
                // モードによって結果文面を変える
                String msg = switch (mode) {
                    case "shura" -> "修羅モード：あなたのブラック耐性は限界突破です。";
                    case "iyashi" -> "癒しモード：心のオアシスが必要です。";
                    case "ai" -> "AI診断：あなたは隠れブラック体質かもしれません。";
                    default -> "通常モード：あなたのブラック度は 72% です（仮）";
                };
                model.addAttribute("resultMessage", msg);
                session.removeAttribute("mode"); // 終了時にモードリセット
                return "result";
            }

            // 次の質問データ（モードごとに分岐可能）
            String qtext = switch (mode) {
                case "shura" -> "上司に理不尽な要求をされたことがありますか？";
                case "iyashi" -> "最近リフレッシュできていますか？";
                case "ai" -> "AIが管理する職場、どう思いますか？";
                default -> "次の質問です（仮 " + (questionId + 1) + "）";
            };

            model.addAttribute("question", new QuestionVM(questionId + 1, qtext));
            return "question";
        }
        
       
            @GetMapping("/nework")
            public String showLinks() {
                return "nework"; 
            }


    @GetMapping("/records")  public String records()  { return "records";  }
    @GetMapping("/titles")   public String titles()   { return "titles";   }
    @GetMapping("/work")     public String work()     { return "work";     }

    public record UserVM(
        String department, String name, String title,
        int days, String lastCheckedAt,
        int level, int expPercent, String character, String characterName
    ) {}
}


