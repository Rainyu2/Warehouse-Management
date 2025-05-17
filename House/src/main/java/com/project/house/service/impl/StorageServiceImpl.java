package com.project.house.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.house.entity.Storage;
import com.project.house.mapper.StorageMapper;
import com.project.house.service.StorageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage> implements StorageService {
    @Resource
    private StorageMapper storageMapper;
    @Override
    public List<Storage> listAll() {
        return baseMapper.pageStorage();
    }
    @Override
    public IPage pageStorage(Page<Storage> page, LambdaQueryWrapper<Storage> wrapper) {
        return baseMapper.pageStorage(page, wrapper);
    }

}
