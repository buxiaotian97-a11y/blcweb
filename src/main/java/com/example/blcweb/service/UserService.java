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

    /** 名前がすでに使われているか */
    public boolean existsByName(String name) {
        return loginRepository.findByName(name).isPresent();
    }

    /** 新規登録してユーザーを返す */
    @Transactional
    public UserEntity register(UserRegisterForm form) {
        UserEntity user = new UserEntity();
        user.setName(form.getName());
        user.setDepartmentName(form.getDepartmentName());
        // ★ 今は既存ログインに合わせて「生パスワードのまま保存」
        user.setPassword(form.getPassword());
        
        return loginRepository.save(user);
    }
}
