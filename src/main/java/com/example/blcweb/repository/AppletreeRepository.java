package com.example.blcweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.blcweb.entity.AppletreeEntity;
import java.util.List;

public interface AppletreeRepository extends JpaRepository<AppletreeEntity, Long> {

    // 親なし＆ゴミ箱以外の「りんご」を新しい順に取得
    List<AppletreeEntity> findByParentIdIsNullAndStatusNotOrderByUpdatedAtDesc(Integer status);

    // あるりんごにぶら下がるメッセージ一覧
    List<AppletreeEntity> findByParentIdOrderByCreatedAtAsc(Long parent);
}
