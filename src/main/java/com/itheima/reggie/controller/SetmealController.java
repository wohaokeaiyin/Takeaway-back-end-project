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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@Api(tags = "套餐相关接口")
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
    @ApiOperation(value = "新增套餐接口")
    @CacheEvict(value = "setmealCache",allEntries = true)
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
    @ApiOperation(value = "分页查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "name",value = "套餐名称",required = false),
    })
    public R<Page> page(int page,int pageSize,String name){
        Page<SetmealDto> pageinfo = setmealService.getMyPage(page,pageSize,name);
        return R.success(pageinfo);
    }

    /**
     * 回显
     * @param id
     * @return
     */
    @ApiOperation(value = "查询列表--回显接口")
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
    @ApiOperation(value = "更新套餐接口")
    @CacheEvict(value = "setmealCache",allEntries = true)
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
    @CacheEvict(value = "setmealCache",allEntries = true)
    @ApiOperation(value = "删除套餐接口")
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
    @ApiOperation(value = "修改状态接口")
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
    @ApiOperation(value = "套餐条件查询接口")
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        lambdaQueryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        lambdaQueryWrapper.orderByAsc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(lambdaQueryWrapper);
        return R.success(list);
    }

}
