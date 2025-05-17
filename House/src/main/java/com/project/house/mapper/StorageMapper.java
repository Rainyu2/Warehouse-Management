package com.project.house.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.house.entity.Storage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StorageMapper extends BaseMapper<Storage> {
    List<Storage> pageStorage();

    IPage pageStorage(@Param("page") Page<Storage> page,
                      @Param("ew") LambdaQueryWrapper<Storage> wrapper);
}
