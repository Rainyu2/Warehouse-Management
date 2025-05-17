package com.project.house.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.house.mapper.RecordMapper;
import com.project.house.service.RecordService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.project.house.entity.Record;

import java.util.List;

@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {
    @Resource
    private RecordMapper recordMapper;
    @Override
    public List<Record> selectRecordList() {
        return recordMapper.selectRecordList();
    }
    @Override
    public IPage<Record> pageRecord(Page<Record> page, QueryWrapper<Record> queryWrapper) {
            return recordMapper.pageRecord(page, queryWrapper);
    }
}
