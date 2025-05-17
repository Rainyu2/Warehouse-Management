package com.project.house.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.house.common.QueryPageParam;
import com.project.house.common.Result;
import com.project.house.entity.Menu;
import com.project.house.entity.User;
import com.project.house.service.MenuService;
import com.project.house.service.UserService;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.List;

@RestController
public class TestController {

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @GetMapping("/list")
    public Result list() {
        return Result.success(userService.listAll());
        //return userService.listAll();
    }

    @GetMapping("/findByNo")
    public Result findByNo(
            @RequestParam String no,
            @RequestParam(required = false) String currentId) {

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNo, no);
        // 排除当前编辑的用户
        if (StringUtils.isNotBlank(currentId)) {
            wrapper.ne(User::getId, currentId);
        }
        List list = userService.list(wrapper);
        return list.size() > 0 ? Result.success(list) : Result.fail();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody User user) {
        return userService.save(user)?Result.success():Result.fail();
    }

    //更新
    @PostMapping("/mod")
    public Result mod(@RequestBody User user) {
        return userService.updateById(user)?Result.success():Result.fail();
    }

    //修改
    @PostMapping("/update")
    public boolean update(User user) {
        return userService.updateById(user);
    }

    //新增或修改
    @PostMapping("/saveOrUpdate")
    public boolean saveOrUpdate(User user) {
        return userService.saveOrUpdate(user);
    }

    //删除
    @GetMapping("/del")
    public Result delete(@RequestParam String id) {
        return userService.removeById(id)?Result.success():Result.fail();
    }

    //查询
    @PostMapping("/like")
    public Result like(User name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (String.valueOf(name.getName()).equals("")) {
            wrapper.like(User::getName, name.getName()); //模糊查询
        }
        //wrapper.eq(User::getName, name.getName()); //精确查询
        return Result.success(userService.list(wrapper));
    }

    //分页查询
    @PostMapping("/page")
//    public List<User> page(HashMap map) {
    public List<User> page(@RequestBody QueryPageParam query) {
//        System.out.println(map.get("pageNum"));
//        System.out.println("num==="+query.getPageNum());
//        System.out.println("size==="+query.getPageSize());

        HashMap params = query.getParams();
        /*System.out.println("name==="+params.get("name"));
        System.out.println("no==="+params.get("no"));*/
        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(User::getName, params.get("name"));
        IPage result = userService.page(page, wrapper);
        //System.out.println("总条数："+result.getTotal());
        return result.getRecords();
    }

    //自定义的分页查询
    @PostMapping("/pageC")
    public List<User> pageC(@RequestBody QueryPageParam query) {
        HashMap params = query.getParams();
        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(User::getName, params.get("name"));
        //IPage result = userService.pageC(page);
        IPage result = userService.pageCC(page,wrapper);
        //System.out.println("总条数："+result.getTotal());
        return result.getRecords();
    }


    @PostMapping("/pageC1")
    public Result pageC1(@RequestBody QueryPageParam query) {
        HashMap params = query.getParams();
        String name = (String)params.get("name");
        String sex = (String)params.get("sex");
        String roleId = (String)params.get("roleId");
        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            wrapper.like(User::getName, name);
        }
        if(StringUtils.isNotBlank(sex)){
            wrapper.eq(User::getSex, sex);
        }
        if(StringUtils.isNotBlank(roleId)){
            wrapper.eq(User::getRoleId, roleId);
        }

        //IPage result = userService.pageC(page);
        IPage result = userService.pageCC(page,wrapper);
        //System.out.println("总条数："+result.getTotal());
        return Result.success(result.getRecords(),result.getTotal());
    }

    //登录
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        List list = userService.lambdaQuery().eq(User::getNo, user.getNo())
                .eq(User::getPassword, user.getPassword()).list();

        if(list.size() > 0){
            User user1 = (User) list.get(0);
            List menulist = menuService.lambdaQuery().like(Menu::getMenuRight,user1.getRoleId()).list();
            HashMap res = new HashMap();
            res.put("user",user1);
            res.put("menu",menulist);
            return Result.success(res);
        }
        return Result.fail();
    }


}

