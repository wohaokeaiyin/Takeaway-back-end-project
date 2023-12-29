package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.ShoppingCart;

import java.util.List;

/**
 * @author SunQi
 * @date 2023/12/10 21:03
 */

public interface ShoppingCartService extends IService<ShoppingCart> {
    ShoppingCart myAdd(ShoppingCart shoppingCart);

    List<ShoppingCart> myList();
}
