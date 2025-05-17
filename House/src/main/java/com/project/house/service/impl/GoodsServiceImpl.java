package com.project.house.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.house.entity.GoodsType;
import com.project.house.mapper.GoodsMapper;
import com.project.house.service.GoodsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, GoodsType> implements GoodsService {
    @Resource
    private GoodsMapper goodsMapper;
    @Override
    public List<GoodsType> listAll() {
        return goodsMapper.pageGoods();
    }
    @Override
    public IPage pageGoods(Page<GoodsType> page, LambdaQueryWrapper<GoodsType> wrapper) {
        return goodsMapper.pageGoods(page, wrapper);
    }
}
