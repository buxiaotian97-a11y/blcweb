package com.example.blcweb.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegisterForm {

    @NotBlank(message = "名前を入力してください")
    @Size(max = 100, message = "名前は100文字以内で入力してください")
    private String name;

    @NotBlank(message = "部署名を入力してください")
    @Size(max = 100, message = "部署名は100文字以内で入力してください")
    private String departmentName;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 4, max = 100, message = "パスワードは4〜100文字で入力してください")
    private String password;

    @NotBlank(message = "確認用パスワードを入力してください")
    private String confirmPassword;

    // --- getter / setter ---

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
