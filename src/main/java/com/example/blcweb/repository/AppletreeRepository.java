package com.example.blcweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.blcweb.dto.ThreadMessage;
import com.example.blcweb.entity.AppletreeEntity;
import java.util.List;

public interface AppletreeRepository extends JpaRepository<AppletreeEntity, Long> {

    // 親なし＆ゴミ箱以外の「りんご」を新しい順に取得
    List<AppletreeEntity> findByParentIdIsNullAndStatusNotOrderByUpdatedAtDesc(Integer status);

    // あるりんごにぶら下がるメッセージ一覧
    List<AppletreeEntity> findByParentIdOrderByCreatedAtAsc(Long parent);
    
    // 返信メッセージにユーザー名を付ける
    @Query("""
    	      select new com.example.blcweb.dto.ThreadMessage(
    	        a.id, a.message, a.userId, u.name, a.createdAt
    	      )
    	      from AppletreeEntity a
    	      join UserEntity u on u.id = a.userId
    	      where a.parentId = :appleId
    	      order by a.createdAt asc
    	    """)
    	    List<ThreadMessage> findThreadMessagesWithUserName(@Param("appleId") Long appleId);
}
