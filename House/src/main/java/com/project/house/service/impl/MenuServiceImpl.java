package com.project.house.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.house.entity.Menu;
import com.project.house.mapper.MenuMapper;
import com.project.house.service.MenuService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<Menu> pageMenu() {
        return baseMapper.pageMenu(); // 改用父类提供的baseMapper
    }
}
