package com.project.house.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.house.common.QueryPageParam;
import com.project.house.common.Result;
import com.project.house.entity.Menu;
import com.project.house.entity.Storage;
import com.project.house.entity.User;
import com.project.house.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/storage")
public class StorageController {
    @Autowired
    private StorageService storageService;
    //查询所有
    @GetMapping("/list")
    public Result list() {
        return Result.success(storageService.listAll());
    }
    // 新增保存接口
    @PostMapping("/save")
    public Result save(@RequestBody Storage storage) {
        return storageService.save(storage) ? Result.success() : Result.fail();
    }

    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Storage storage) {
        return storageService.updateById(storage)?Result.success():Result.fail();
    }
    //删除
    @GetMapping("/del") // 改为 GET 请求
    public Result del(@RequestParam String id) { // 接收 ID 参数
        return storageService.removeById(id) ? Result.success() : Result.fail();
    }
    //查询
    @PostMapping("/page")
    public Result page(@RequestBody QueryPageParam query) {
        HashMap params = query.getParams();
        String name = (String)params.get("name");
        Page<Storage> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        LambdaQueryWrapper<Storage> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            wrapper.like(Storage::getName, name);
        }
        IPage result = storageService.pageStorage(page,wrapper);
        return Result.success(result.getRecords(),result.getTotal());
    }

}
