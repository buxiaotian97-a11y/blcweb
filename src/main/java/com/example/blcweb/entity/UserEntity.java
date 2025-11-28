package com.example.blcweb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "department_name", nullable = false, length = 100)
    private String departmentName;
    
    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
    
    public void setId(Long id) {this.id = id; }
    public void setName(String name){ this.name = name; }
    public void setDepartmentName(String departmentName){ this.departmentName = departmentName; }
    public void setPassword(String password) { this.password = password; }
    
    public Long getId() {return id; }
    public String getDepartmentName() { return departmentName; }
    public String getName() { return name; }
    public String getPassword() { return password; }

}
