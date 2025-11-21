package com.example.blcweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blcweb.entity.LoginEntity;

public interface LoginRepository extends JpaRepository<LoginEntity, Long> {
	Optional<LoginEntity> findByName(String name);
}