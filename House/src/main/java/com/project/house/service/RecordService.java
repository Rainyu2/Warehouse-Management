package com.project.house.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import com.project.house.entity.Record;

import java.util.List;

@Service
public interface RecordService extends IService<Record> {
    List<Record> selectRecordList();
    IPage<Record> pageRecord(Page<Record> page, @Param("ew") QueryWrapper<Record> queryWrapper);
}
