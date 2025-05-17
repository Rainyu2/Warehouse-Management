package com.project.house.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.house.entity.Menu;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MenuService extends IService<Menu> {
    List<Menu> pageMenu();
}
