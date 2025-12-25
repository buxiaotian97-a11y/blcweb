package com.example.blcweb.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blcweb.entity.UserEntity;
import com.example.blcweb.repository.DataSetRepository;
import java.util.Optional;

@Service
public class DataSetService {

    private final DataSetRepository dataSetRepo;

    public DataSetService(DataSetRepository dataSetRepo) {
        this.dataSetRepo = dataSetRepo;
    }

    @Transactional
    public void save(Long userId, String name, String departmentName) {
    	
    	 if (dataSetRepo.existsByNameAndIdNot(name, userId)) {
    	        throw new IllegalArgumentException("同じ名前のユーザーが既に存在します");
    	    }
    	
    	// 既存ユーザーの取得
        UserEntity user = dataSetRepo.findById(userId)
        		.orElseThrow();
        
        // 名前と事業部のみ更新
        user.setName(name);
        user.setDepartmentName(departmentName);
        
        // saveするとupdateがとぶ
        dataSetRepo.save(user);
    }

    @Transactional(readOnly = true)
    public List<UserEntity> findAll() {
        return dataSetRepo.findAll();
    }
    
    public Optional<UserEntity> findLatest() {
        return dataSetRepo.findTopByOrderByIdDesc();
    }

}
