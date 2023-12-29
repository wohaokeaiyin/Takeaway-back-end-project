package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author SunQi
 * @date 2023/12/5 20:02
 */

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    Page<SetmealDto> getMyPage(int page, int pageSize, String name);


    SetmealDto getByIdWithSetmealDish(Long id);

    void updateWithSelmealDish(SetmealDto setmealDto);

    void myDelete(List<Long> ids);

    void changeStatus(int status,List<Long> ids);
}
