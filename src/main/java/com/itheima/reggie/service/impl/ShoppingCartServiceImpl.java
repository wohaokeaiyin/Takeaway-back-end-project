package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.mapper.ShoppingCartMapper;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author SunQi
 * @date 2023/12/10 21:03
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCart myAdd(ShoppingCart shoppingCart) {
        log.info("购物车数据：{}",shoppingCart);
        //设置用户id，指定当前哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //添加到购物车的 是菜品
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,shoppingCart.getUserId());
        if(dishId!=null){
            lambdaQueryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }
        else{
            //套餐
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }


        //查询菜品或者套餐是否在购物车中
        ShoppingCart cart = shoppingCartMapper.selectOne(lambdaQueryWrapper);

        //如果已经存在，在原来数量上+1
        if (cart != null) {
            Integer number = cart.getNumber();
            cart.setNumber(number+1);
            shoppingCartMapper.updateById(cart);
        }
        else{
            //如果不存在，存到购物车中
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
            cart= shoppingCart;
        }
        return cart;

    }

    @Override
    public List<ShoppingCart> myList() {

        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        lambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartMapper.selectList(lambdaQueryWrapper);

        return list;
    }
}
