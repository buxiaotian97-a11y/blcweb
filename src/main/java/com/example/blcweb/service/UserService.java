package com.example.blcweb.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blcweb.entity.UserEntity;
import com.example.blcweb.form.UserRegisterForm;
import com.example.blcweb.repository.LoginRepository;

@Service
public class UserService {

    private final LoginRepository loginRepository;

    public UserService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    private String normalizeName(String name) {
        return name == null ? null : name.strip();
    }

    /** 名前がすでに使われているか */
    public boolean existsByName(String name) {
        String normalized = normalizeName(name);
        return normalized != null && loginRepository.findByName(normalized).isPresent();
    }

    /** 新規登録してユーザーを返す */
    @Transactional
    public UserEntity register(UserRegisterForm form) {
        String name = normalizeName(form.getName());
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name is blank");
        }

        // ここで重複チェック（Controller側でやってても保険で）
        if (loginRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("name already exists");
        }

        UserEntity user = new UserEntity();
        user.setName(name);
        user.setDepartmentName(form.getDepartmentName()); // ここもtrimしたければ同様に
        user.setPassword(form.getPassword()); // パスは基本trimしない

        return loginRepository.save(user);
    }
}
