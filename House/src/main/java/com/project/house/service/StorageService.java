package com.project.house.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.house.entity.Storage;
import com.project.house.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StorageService extends IService<Storage> {
    List<Storage> listAll();

    IPage pageStorage(Page<Storage> page, LambdaQueryWrapper<Storage> wrapper);
}
