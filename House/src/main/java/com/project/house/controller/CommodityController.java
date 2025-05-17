package com.project.house.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.house.common.QueryPageParam;
import com.project.house.common.Result;
import com.project.house.entity.Commodity;
import com.project.house.entity.GoodsType;
import com.project.house.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/commodity")
public class CommodityController {
    @Autowired
    private CommodityService commodityService;
    @GetMapping("/list")
    public Result list() {
        return Result.success(commodityService.listCommodity());
    }
    // 新增保存接口
    @PostMapping("/save")
    public Result save(@RequestBody Commodity commodity) {
        return commodityService.save(commodity) ? Result.success() : Result.fail();
    }

    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Commodity commodity) {
        return commodityService.updateById(commodity)?Result.success():Result.fail();
    }
    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id) { // 接收 ID 参数
        return commodityService.removeById(id) ? Result.success() : Result.fail();
    }
    //查询
    @PostMapping("/page")
    public Result page(@RequestBody QueryPageParam query) {
        HashMap params = query.getParams();
        String name = (String)params.get("name");
        String storage = (String)params.get("storage");
        String goodsType = (String)params.get("goodsType");

        Page<Commodity> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        LambdaQueryWrapper<Commodity> wrapper = new LambdaQueryWrapper<>();

        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            wrapper.like(Commodity::getName, name);
        }
        if(StringUtils.isNotBlank(storage) &&!"null".equals(storage)){
            wrapper.eq(Commodity::getStorage, storage);
        }
        if(StringUtils.isNotBlank(goodsType) &&!"null".equals(goodsType)){
            wrapper.eq(Commodity::getGoodsType, goodsType);
        }

        IPage result = commodityService.pageCommodity(page,wrapper);
        return Result.success(result.getRecords(),result.getTotal());
    }
}
