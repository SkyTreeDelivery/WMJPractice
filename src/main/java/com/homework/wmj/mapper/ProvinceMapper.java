package com.homework.wmj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homework.wmj.pojo.po.Province;

public interface ProvinceMapper extends BaseMapper<Province> {
    int insert(Province record);

    int insertSelective(Province record);
}