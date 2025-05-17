package com.project.house.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.house.entity.GoodsType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GoodsService extends IService<GoodsType> {
    List<GoodsType> listAll();
    IPage pageGoods(Page<GoodsType> page, LambdaQueryWrapper<GoodsType> wrapper);

}
