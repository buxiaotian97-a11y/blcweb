package com.example.blcweb.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.blcweb.dto.ThreadMessage;
import com.example.blcweb.entity.AppletreeEntity;
import com.example.blcweb.entity.UserEntity;
import com.example.blcweb.service.AppletreeService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/appletree")
public class AppletreeController {

    private final AppletreeService appletreeService;

    public AppletreeController(AppletreeService appletreeService) {
        this.appletreeService = appletreeService;
    }

    /**
     * ツリー画面：
     * 親りんご一覧を取得して木にぶら下げる
     */
    @GetMapping("/tree")
    public String showTree(Model model) {
        List<AppletreeEntity> apples = appletreeService.getRootApples();
        model.addAttribute("apples", apples);
        return "appletree/tree"; // → templates/apple/tree.html
    }

    /**
     * りんご（スレッドの頭）を新規作成
     */
    @PostMapping("/create")
    public String createApple(
            @RequestParam("userId") long userId,        // TODO: 本番はセッション/ログインユーザーから取得
            @RequestParam("title") String title,
            @RequestParam("message") String message) {

        appletreeService.createApple(userId, title, message);
        return "redirect:/appletree/tree";
    }

    /**
     * りんご詳細（スレッド画面）：
     * 親りんご＋その中のメッセージ一覧を表示
     */
    @GetMapping("/{appleId}")
    public String showThread(
            @PathVariable("appleId") long appleId,
            Model model) {

        // 親りんご本体（頭の投稿）
        AppletreeEntity root = appletreeService.getApple(appleId);

        // そのりんごにぶら下がるメッセージ一覧
        List<ThreadMessage> messages = appletreeService.getThreadMessagesWithUser(appleId);
        model.addAttribute("messages", messages);


        model.addAttribute("root", root);
        model.addAttribute("messages", messages);
        return "appletree/thread"; // → templates/apple/thread.html
    }
    
 // 新規りんご入力画面
    @GetMapping("/new")
    public String showCreateForm() {
        return "appletree/new";   // templates/appletree/new.html を表示
    }

    /**
     * りんごへの返信投稿
     */
    @PostMapping("/{appleId}/reply")
    public String reply(
        @PathVariable("appleId") long appleId,
        @RequestParam("message") String message,
        HttpSession session
    ) {
        UserEntity loginUser = (UserEntity) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/title-page";

        appletreeService.replyMessage(appleId, loginUser.getId(), message); // ★ここ

        return "redirect:/appletree/" + appleId;
    }

}
