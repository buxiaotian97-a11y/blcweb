package com.example.blcweb.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.blcweb.entity.UserEntity;
import com.example.blcweb.repository.DataSetRepository;

@SpringBootTest
@Transactional
class DataSetServiceTest {

    @Autowired
    DataSetService dataSetService;

    @Autowired
    DataSetRepository dataSetRepository;

    @Test
    void save_既存ユーザーの名前と事業部が更新されること() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setName("旧ユーザー名");
        user.setDepartmentName("旧事業部");
        user.setPassword("old-password");
        user = dataSetRepository.save(user);
        Long userId = user.getId();

        // Act
        dataSetService.save(userId, "新しい名前", "新しい事業部");

        // Assert
        UserEntity updated = dataSetRepository.findById(userId)
                .orElseThrow(() -> new AssertionError("ユーザーが存在しません"));

        assertEquals("新しい名前", updated.getName());
        assertEquals("新しい事業部", updated.getDepartmentName());
        assertEquals("old-password", updated.getPassword());
    }

    @Test
    void findAll_全件取得できること() {
        // Arrange
        UserEntity u1 = new UserEntity();
        u1.setName("ユーザー1");
        u1.setDepartmentName("事業部1");
        u1.setPassword("pass1");
        dataSetRepository.save(u1);

        UserEntity u2 = new UserEntity();
        u2.setName("ユーザー2");
        u2.setDepartmentName("事業部2");
        u2.setPassword("pass2");
        dataSetRepository.save(u2);

        // Act
        List<UserEntity> all = dataSetService.findAll();

        // Assert
        assertNotNull(all);
        assertTrue(all.size() >= 2, "少なくとも2件は返ってきてほしい");
    }

    @Test
    void findLatest_最後に登録したユーザーが返ること() {
        // Arrange
        UserEntity oldUser = new UserEntity();
        oldUser.setName("古いユーザー");
        oldUser.setDepartmentName("古い事業部");
        oldUser.setPassword("old-pass");
        dataSetRepository.save(oldUser);

        UserEntity newUser = new UserEntity();
        newUser.setName("新しいユーザー");
        newUser.setDepartmentName("新しい事業部");
        newUser.setPassword("new-pass");
        newUser = dataSetRepository.save(newUser);

        // Act
        Optional<UserEntity> latestOpt = dataSetService.findLatest();

        // Assert
        assertTrue(latestOpt.isPresent(), "最新ユーザーが取得できませんでした");
        UserEntity latest = latestOpt.get();
        assertEquals(newUser.getId(), latest.getId());
        assertEquals("新しいユーザー", latest.getName());
        assertEquals("新しい事業部", latest.getDepartmentName());
    }
}
