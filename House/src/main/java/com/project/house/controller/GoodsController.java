package com.project.house.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.house.common.QueryPageParam;
import com.project.house.common.Result;
import com.project.house.entity.GoodsType;
import com.project.house.entity.Storage;
import com.project.house.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @RequestMapping("/list")
    public Result list() {
        return Result.success(goodsService.listAll());
    }
    // 新增保存接口
    @PostMapping("/save")
    public Result save(@RequestBody GoodsType goodsType) {
        return goodsService.save(goodsType) ? Result.success() : Result.fail();
    }

    //更新
    @PostMapping("/update")
    public Result update(@RequestBody GoodsType goodsType) {
        return goodsService.updateById(goodsType)?Result.success():Result.fail();
    }
    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id) { // 接收 ID 参数
        return goodsService.removeById(id) ? Result.success() : Result.fail();
    }
    //查询
    @PostMapping("/page")
    public Result page(@RequestBody QueryPageParam query) {
        HashMap params = query.getParams();
        String name = (String)params.get("name");
        Page<GoodsType> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        LambdaQueryWrapper<GoodsType> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            wrapper.like(GoodsType::getName, name);
        }
        IPage result = goodsService.pageGoods(page,wrapper);
        return Result.success(result.getRecords(),result.getTotal());
    }
}
