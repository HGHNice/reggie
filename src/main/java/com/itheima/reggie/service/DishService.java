package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表: dish、dish_flavor
    public void savaWithFlavor(DishDto dishDto);
    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);
    //更新菜品信息与口味
    public void updateWithFlavor(DishDto dishDto);

    boolean batchUpdateStatusByIds(Integer status, List<Long> ids);

    void batchDeleteByIds(List<Long> ids);
}
