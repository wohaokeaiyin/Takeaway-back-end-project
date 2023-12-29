package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

import java.util.List;

/**
 * @author SunQi
 * @date 2023/12/5 20:01
 */

public interface DishService extends IService<Dish> {
    void savaWithFlavor(DishDto dishDto);

    Page<DishDto> myPage(int page, int pageSize, String name);

    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);

    List<DishDto> myList(Dish dish);

    void deleteWithFlavor(List<Long> ids);

    void changeStatus(int status, List<Long> ids);
}
