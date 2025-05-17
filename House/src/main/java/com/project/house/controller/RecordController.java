package com.project.house.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.house.common.QueryPageParam;
import com.project.house.common.Result;
import com.project.house.entity.Record;
import com.project.house.entity.Commodity;
import com.project.house.service.CommodityService;
import com.project.house.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.project.house.service.RecordService;
import java.util.HashMap;


@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private CommodityService commodityService;

    @GetMapping("/list")
    public Result getRecordList() {
        return Result.success(recordService.selectRecordList());
    }

    //查询
    @PostMapping("/page")
    public Result page(@RequestBody QueryPageParam query) {
        HashMap params = query.getParams();
        String name = (String)params.get("name");
        String storage = (String)params.get("storage");
        String goodsType = (String)params.get("goodsType");
        String roleId = (String)params.get("roleId");
        String userId = (String) params.get("userId");

        Page<Record> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply(" a.goods = b.id and b.storage = c.id and b.goodsType = d.id ");
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            queryWrapper.like("b.name",name);
        }
        if(StringUtils.isNotBlank(storage) &&!"null".equals(storage)){
            queryWrapper.eq("c.id",storage);
        }
        if(StringUtils.isNotBlank(goodsType) &&!"null".equals(goodsType)){
            queryWrapper.eq("d.id",goodsType);
        }

        if ("2".equals(roleId)){
            queryWrapper.apply("a.userId = "+userId);
        }

        IPage result = recordService.pageRecord(page,queryWrapper);
        return Result.success(result.getRecords(),result.getTotal());
    }

    // 新增保存接口
    @PostMapping("/save")
    public Result save(@RequestBody Record record) {
        Commodity commodity = commodityService.getById(record.getGoods());
        int n = record.getCount();
        //出库
        if ("2".equals( record.getAction())){
            n = -n;
            record.setCount(n);
        }
        int newNum = commodity.getCount() + n;
        commodity.setCount(newNum);
        commodityService.updateById(commodity);
        return recordService.save(record) ? Result.success() : Result.fail();
    }
}
