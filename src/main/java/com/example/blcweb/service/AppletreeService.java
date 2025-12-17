package com.example.blcweb.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blcweb.constant.AppleStatus;
import com.example.blcweb.dto.ThreadMessage;
import com.example.blcweb.entity.AppletreeEntity;
import com.example.blcweb.repository.AppletreeRepository;

@Service
@Transactional
public class AppletreeService {

    private final AppletreeRepository appletreeRepository;

    public AppletreeService(AppletreeRepository appletreeRepository) {
        this.appletreeRepository = appletreeRepository;
    }

    // 木にぶら下げる「りんご一覧」（親＝null ＆ ゴミ箱以外）
    public List<AppletreeEntity> getRootApples() {
        return appletreeRepository
                .findByParentIdIsNullAndStatusNotOrderByUpdatedAtDesc(AppleStatus.TRASH);
    }

    // 新しい りんご（スレッドの頭）を作る
    public AppletreeEntity createApple(long userId, String title, String message) {
        AppletreeEntity apple = new AppletreeEntity();
        apple.setUserId(userId);
        apple.setParentId(null);                     // 親なし = りんご本体
        apple.setTitle(title);
        apple.setMessage(message);
        apple.setStatus(AppleStatus.ACTIVE);       // 新規は必ず通常状態
        return appletreeRepository.save(apple);
    }

    // 既存りんごに返信メッセージをぶら下げる
    public AppletreeEntity replyMessage(long parentAppleId, long userId, String message) {

        // 親りんごを取得
        AppletreeEntity parentId = appletreeRepository.findById(parentAppleId)
                .orElseThrow(() -> new IllegalArgumentException("apple not found"));

        // 親がゴミ箱なら返信禁止
        if (parentId.getStatus() == AppleStatus.TRASH) {
            throw new IllegalStateException("ゴミ箱のスレッドには返信できません。");
        }

        AppletreeEntity reply = new AppletreeEntity();
        reply.setParentId(parentAppleId);            // どのスレッドかを紐付け
        reply.setUserId(userId);
        reply.setMessage(message);
        reply.setStatus(AppleStatus.ACTIVE);       // 返信も通常状態で
        return appletreeRepository.save(reply);
    }

    // あるりんごの中身（メッセージ一覧）取得
    public List<AppletreeEntity> getThreadMessages(long appleId) {
        return appletreeRepository
                .findByParentIdOrderByCreatedAtAsc(appleId);
    }

    public AppletreeEntity getApple(long appleId) {
        return appletreeRepository.findById(appleId)
                .orElseThrow(() -> new IllegalArgumentException("apple not found"));
    }
    
    public List<ThreadMessage> getThreadMessagesWithUser(long appleId){
    	  return appletreeRepository.findThreadMessagesWithUserName(appleId);
    	}

}
