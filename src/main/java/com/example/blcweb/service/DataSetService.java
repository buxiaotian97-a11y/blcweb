package com.example.blcweb.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blcweb.entity.DataSetEntity;
import com.example.blcweb.repository.DataSetRepository;

@Service
public class DataSetService {

    private final DataSetRepository dataSetRepo;

    public DataSetService(DataSetRepository dataSetRepo) {
        this.dataSetRepo = dataSetRepo;
    }

    @Transactional
    public void save(String name, String departmentName) {
        DataSetEntity data = new DataSetEntity();
        data.setName(name);
        data.setDepartmentName(departmentName);
        dataSetRepo.save(data);
    }

    @Transactional(readOnly = true)
    public List<DataSetEntity> findAll() {
        return dataSetRepo.findAll();
    }
}
