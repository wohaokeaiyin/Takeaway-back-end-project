package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.CategoryMappper;
import com.itheima.reggie.mapper.DishFlavorMapper;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SunQi
 * @date 2023/12/5 20:02
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish>
        implements DishService{

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryMappper categoryMappper;

    @Transactional
    @Override
    public void savaWithFlavor(DishDto dishDto) {
        //保存到菜品表dish
        dishMapper.insert(dishDto);

        Long dishId = dishDto.getId();

        //将每一个flavor的id赋值为上述的dishId
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Page<DishDto> myPage(int page, int pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(name!=null,Dish::getName,name);
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishMapper.selectPage(dishPage,dishLambdaQueryWrapper);

        //page对象拷贝
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");

        //records具体记录拷贝
        List<Dish> records = dishPage.getRecords();
        List<DishDto> collect = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //拷贝属性
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            //根据dish里的categoryid去category表中查找名字，再赋值给dishDto
            // 单纯是因为这个类有categoryname属p性，因此才又重新复制过来
            Category category = categoryMappper.selectById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        //新的page重新设置records
        dishDtoPage.setRecords(collect);

        return dishDtoPage;
    }

    /**
     * 回显
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        DishDto dishDto = new DishDto();
        Dish dish = dishMapper.selectById(id);
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    /**
     * 更新菜品
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表
        dishMapper.updateById(dishDto);

        //将dish_flavor中的旧值删除
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        //添加新的flavor
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public List<DishDto> myList(Dish dish) {
//构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishMapper.selectList(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryMappper.selectById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        return dishDtoList;
    }

    @Transactional
    @Override
    public void deleteWithFlavor(List<Long> ids) {
        //检查状态，是否可以删除
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Dish::getId,ids);
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        Integer count = dishMapper.selectCount(lambdaQueryWrapper);
        if(count > 0 ){
            throw new CustomException("商品正在售卖中，不能删除");
        }

        //删除dish表的数据
        dishMapper.deleteBatchIds(ids);

        //删除dish_flavor表中的数据
        LambdaQueryWrapper<DishFlavor> flavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        flavorLambdaQueryWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(flavorLambdaQueryWrapper);
    }

    @Override
    public void changeStatus(int status, List<Long> ids) {
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Dish::getId,ids);
        List<Dish> dishes = dishMapper.selectList(lambdaQueryWrapper);
        for (Dish dish : dishes) {
            if(dish.getStatus() == 0){
                dish.setStatus(1);
            }
            else{
                dish.setStatus(0);
            }
            dishMapper.updateById(dish);
        }
    }
}
