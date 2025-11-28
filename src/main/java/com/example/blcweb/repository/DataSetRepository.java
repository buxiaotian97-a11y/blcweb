package com.example.blcweb.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.blcweb.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSetRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByName(String name);
    Optional<UserEntity> findByDepartmentName(String departmentName);
    Optional<UserEntity> findTopByOrderByIdDesc();
}