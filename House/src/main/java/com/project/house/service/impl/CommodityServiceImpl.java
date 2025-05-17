package com.project.house.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.house.entity.Commodity;
import com.project.house.mapper.CommodityMapper;
import com.project.house.service.CommodityService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {
    @Resource
    private CommodityMapper commodityMapper;

    @Override
    public List<Commodity> listCommodity() {
        return commodityMapper.listCommodity();
    }

    @Override
    public IPage pageCommodity(Page<Commodity> page, LambdaQueryWrapper<Commodity> wrapper) {
        return commodityMapper.pageCommodity(page, wrapper);
    }
}
