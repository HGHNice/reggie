package com.itheima.reggie.comtroller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealDishController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page>page(int page,int pageSize,String name){
        Page<Setmeal> pageinfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtopage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageinfo,queryWrapper);
        BeanUtils.copyProperties(pageinfo,dtopage,"records");
        List<Setmeal> records = pageinfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String category_name = category.getName();
                setmealDto.setCategoryName(category_name);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtopage.setRecords(list);
        return R.success(dtopage);
    }

    /**
     * 套餐插入
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> insert(@RequestBody SetmealDto setmealDto){
        setmealService.savaWithDish(setmealDto);
        return R.success("套餐插入成功！");

    }

    /**
     * 在修改套餐窗口显示菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getByIdWithSetmealDto(id);
        return R.success(setmealDto);
    }

    /**
     * 用户修改提交接口
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update_SetmealDto(@RequestBody SetmealDto setmealDto){
        if (setmealDto==null){
            return R.error("请求异常");
        }
        if (setmealDto.getSetmealDishes()==null){
            return R.error("套餐没有菜品,请添加套餐");
        }
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Long setmealId = setmealDto.getId();
        LambdaQueryWrapper<SetmealDish>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealId);
        setmealDishService.remove(queryWrapper);
        //为setmeal_dish表填充相关的属性
        for (SetmealDish setmealDish:setmealDishes){
            setmealDish.setSetmealId(setmealId);
        }

        //批量把setmealDish保存到setmeal_dish表
        setmealDishService.saveBatch(setmealDishes);
        setmealService.updateById(setmealDto);

        return R.success("套餐修改成功");
    }
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        if (ids.size()==0){
            return R.error("请选择菜品");
        }
        setmealService.removeWithDish(ids);
        return R.success("删除成功！ ");
    }
    @PostMapping("/status/{status}")
    public R<String> update_status(@PathVariable Integer status,@RequestParam String ids){
        //菜品的状态(1为售卖,0为停售)由前端修改完成后通过请求路径占位符的方式传到后端,然后请求参数的类型设置为list类型,这样就可以进行批量或者单个菜品进行修改售卖状态
        if (setmealService.batchUpdateStatusByIds(status, ids))
            return R.success("套餐的售卖状态已更改！");
        else
            return R.error("售卖状态无法更改！");

    }

    /**
     *
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>>list(Setmeal setmeal) {  //键值对直接使用实体来接收
        LambdaQueryWrapper<Setmeal>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

}
