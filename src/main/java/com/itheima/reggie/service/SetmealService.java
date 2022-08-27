package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {
    //新增套餐
    public void savaWithDish(SetmealDto setmealDto);

    public SetmealDto getByIdWithSetmealDto(Long id);

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    public boolean batchUpdateStatusByIds(Integer status, String ids);
}
