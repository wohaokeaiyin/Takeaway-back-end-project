package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SunQi
 * @date 2023/12/6 11:13
 */
@RestController
@Slf4j
@RequestMapping("dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     *
     * 菜品新增
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody  DishDto dishDto){
        dishService.savaWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 菜品分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("page")
    public R<Page> page(int page,int pageSize,String name){
        Page<DishDto> pageinfo = dishService.myPage(page,pageSize,name);
        return R.success(pageinfo);
    }

    /**
     * 查询dishDto
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 更新DishDto
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> saveDishDto(@RequestBody  DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("更新菜品成功");
    }

    /**
     * 根据id查询dish
     * @param dish
     * @return
     */
//    @GetMapping("list")
//    public R<List<Dish>> list(Dish dish){
//        List<Dish> list = dishService.myList(dish);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> list = dishService.myList(dish);
        return R.success(list);
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteDish(@RequestParam List<Long> ids){
        dishService.deleteWithFlavor(ids);
        return R.success("删除菜品成功");
    }

    @PostMapping("status/{status}")
    public R<String> changeStatus(@PathVariable int status,@RequestParam List<Long> ids){
        dishService.changeStatus(status,ids);
        return R.success("改变菜品状态成功");
    }
}
