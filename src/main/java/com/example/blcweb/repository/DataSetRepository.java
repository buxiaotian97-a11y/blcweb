package com.example.blcweb.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.blcweb.entity.DataSetEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSetRepository extends JpaRepository<DataSetEntity, Long> {
    Optional<DataSetEntity> findByName(String name);
    Optional<DataSetEntity> findByDepartmentName(String departmentName);
}