package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author SunQi
 * @date 2023/12/7 21:02
 */
@RestController
@RequestMapping("setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;


    @Autowired
    private DishService dishService;

    /**
     * 新增套餐，插入到setmeal和setmealDish两个表
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息：{}",setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 分类查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("page")
    public R<Page> page(int page,int pageSize,String name){
        Page<SetmealDto> pageinfo = setmealService.getMyPage(page,pageSize,name);
        return R.success(pageinfo);
    }

    /**
     * 回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id){
        SetmealDto byIdWithSetmealDish = setmealService.getByIdWithSetmealDish(id);
        return R.success(byIdWithSetmealDish);
    }

    /**
     * 更新套餐
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> saveDishDto(@RequestBody  SetmealDto setmealDto){
        setmealService.updateWithSelmealDish(setmealDto);
        return R.success("更新套餐成功");
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}");
        setmealService.myDelete(ids);
        return R.success("删除套餐成功");
    }

    /**
     * 修改起售或者停售
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("status/{status}")
    public R<String> changeStatus(@PathVariable int status, @RequestParam List<Long> ids){
        log.info("ids:{}",ids);
        setmealService.changeStatus(status,ids);
        return R.success("修改状态成功");
    }

//    /**
//     * 返回套餐中的dish
//     * @param categoryId
//     * @param status
//     * @return
//     */
//    @GetMapping("list")
//    public R<List<Dish>> list(@RequestParam int categoryId,@RequestParam int status){
//        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(Setmeal::getCategoryId,categoryId);
//        Setmeal setmeal = setmealService.getOne(lambdaQueryWrapper);
//
//        if(setmeal!=null){
//            LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
//            lambdaQueryWrapper1.eq(SetmealDish::getSetmealId,setmeal.getId());
//            List<SetmealDish> list = setmealDishService.list(lambdaQueryWrapper1);
//            LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            List<Long> ids = new ArrayList<>();
//            for (SetmealDish setmealDish : list) {
//                ids.add(setmealDish.getDishId());
//            }
//            dishLambdaQueryWrapper.in(Dish::getId,ids);
//            List<Dish> dishes = dishService.list(dishLambdaQueryWrapper);
//            return R.success(dishes);
//        }
//
//        return R.success(null);
//    }

    @GetMapping("list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        lambdaQueryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        lambdaQueryWrapper.orderByAsc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(lambdaQueryWrapper);
        return R.success(list);
    }

}
