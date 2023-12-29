package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.CategoryMappper;
import com.itheima.reggie.mapper.SetmealDishMapper;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SunQi
 * @date 2023/12/5 20:06
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryMappper categoryMappper;


    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存到setmeal表
        setmealMapper.insert(setmealDto);

        //设置setmealid信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存到setmealdish表
        setmealDishService.saveBatch(setmealDishes);
    }

    @Transactional
    @Override
    public Page<SetmealDto> getMyPage(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(name!=null,Setmeal::getName,name);
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealMapper.selectPage(setmealPage,setmealLambdaQueryWrapper);

        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> collect = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryMappper.selectById(categoryId);
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(collect);

        return setmealDtoPage;
    }

    @Override
    public SetmealDto getByIdWithSetmealDish(Long id) {
        Setmeal setmeal = setmealMapper.selectById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(setmeal!=null,SetmealDish::getSetmealId,setmeal.getId());
        List<SetmealDish> list = setmealDishService.list(setmealDishLambdaQueryWrapper);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    @Transactional
    @Override
    public void updateWithSelmealDish(SetmealDto setmealDto) {
        //更新setmeal表
        setmealMapper.updateById(setmealDto);

        //删除setmealdish表中的旧值
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishMapper.delete(setmealDishLambdaQueryWrapper);

        //添加setmealID,插入到表中
        Long setmealId = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<SetmealDish> collect = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());


        setmealDishService.saveBatch(collect);

    }

    @Transactional
    @Override
    public void myDelete(List<Long> ids) {
        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        //相当于select count(*) from setmeal where id in (1,2,3) and status = 1
        lambdaQueryWrapper1.in(Setmeal::getId,ids);
        lambdaQueryWrapper1.eq(Setmeal::getStatus,1);
        Integer count = setmealMapper.selectCount(lambdaQueryWrapper1);
        if(count > 0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        //删除setmeal表中的
        setmealMapper.deleteBatchIds(ids);
        //删除setmealDish表中的
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);

    }

    @Override
    public void changeStatus(int status,List<Long> ids) {
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId, ids);

        List<Setmeal> setmeals = setmealMapper.selectList(lambdaQueryWrapper);
        for (Setmeal setmeal : setmeals) {
            if(setmeal.getStatus() == 0){
                setmeal.setStatus(1);
            }
            else{
                setmeal.setStatus(0);
            }
            setmealMapper.updateById(setmeal);
        }
    }
}
