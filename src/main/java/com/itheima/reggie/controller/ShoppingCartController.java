package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SunQi
 * @date 2023/12/10 21:04
 */
@RestController
@RequestMapping("shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 将菜品或者套餐添加到购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        ShoppingCart shoppingCart1 = shoppingCartService.myAdd(shoppingCart);
        return R.success(shoppingCart1);
    }

    /**
     * 根据 用户id查看购物车信息
     * @return
     */
    @GetMapping("list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车");
        List<ShoppingCart> result = shoppingCartService.myList();
        return R.success(result);
    }


    @DeleteMapping("clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(lambdaQueryWrapper);
        return R.success("清空购物车成功");
    }
}
