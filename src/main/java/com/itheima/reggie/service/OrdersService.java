package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.entity.ShoppingCart;

import java.util.List;

public interface OrdersService extends IService<Orders> {

    public void submit(Orders orders);
    public List<ShoppingCart> againAdd(List<OrderDetail> orderDetailList);
}
