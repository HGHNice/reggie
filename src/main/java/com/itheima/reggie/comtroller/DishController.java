package com.itheima.reggie.comtroller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> sava(@RequestBody DishDto dishDto){
        dishService.savaWithFlavor(dishDto);
        return R.success("新增成功！");
    }
    @GetMapping("/page")
    public R<Page>page(int page,int pageSize,String name){
        Page<Dish>pageinfo = new Page<>(page,pageSize);
        Page<DishDto>dishDtoPage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageinfo,queryWrapper);  //此时已经查出来列表 但是菜品分类没有查出来 所以需要下面的拷贝操作
        BeanUtils.copyProperties(pageinfo,dishDtoPage,"records");
        List<Dish> records = pageinfo.getRecords();
        List<DishDto>list= records.stream().map((item)->{
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功！");
    }
    // 批量或者单个改变菜品的销售状态
    @PostMapping("/status/{status}")
    public R<String> updateSaleStatus(@PathVariable("status") Integer status, @RequestParam List<Long> ids) {
        //菜品的状态(1为售卖,0为停售)由前端修改完成后通过请求路径占位符的方式传到后端,然后请求参数的类型设置为list类型,这样就可以进行批量或者单个菜品进行修改售卖状态
        if (dishService.batchUpdateStatusByIds(status, ids))
            return R.success("菜品的售卖状态已更改！");
        else
            return R.error("售卖状态无法更改！");
    }

    //批量删除菜品
    @DeleteMapping
    public R<String> batchDelete(@RequestParam("ids") List<Long> ids) {
        dishService.batchDeleteByIds(ids);
        return R.success("成功删除菜品！");
    }

}
