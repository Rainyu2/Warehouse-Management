package com.project.house.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.house.entity.Commodity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommodityService extends IService<Commodity> {

    IPage pageCommodity(Page<Commodity> page, @Param("ew") LambdaQueryWrapper<Commodity> wrapper);

    List<Commodity> listCommodity();
}
