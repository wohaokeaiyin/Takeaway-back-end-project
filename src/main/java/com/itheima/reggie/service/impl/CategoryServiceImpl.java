package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMappper;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SunQi
 * @date 2023/12/1 19:47
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMappper, Category>
        implements CategoryService {

    @Autowired
    private CategoryMappper categoryMappper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public R mySave(Category category) {
        categoryMappper.insert(category);
        return R.success("新增分类成功");
    }

    @Override
    public R<Page> myPage(int page, int pageSize) {
        Page<Category> page1 = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        categoryMappper.selectPage(page1,lambdaQueryWrapper);

        return R.success(page1);

    }

    /**
     * 删除之间判断
     * @param id
     */
    @Override
    public void myDelete(Long id) {
//        categoryMappper.deleteById(id);
        //查询当前分类是否关联了菜品，是，抛出一个业务异常
        LambdaQueryWrapper<Dish> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Dish::getCategoryId,id);
        Integer count1 = dishMapper.selectCount(lambdaQueryWrapper1);
        if(count1 > 0){
            //是，抛出一个业务异常
            throw new CustomException("分类关联了菜品，无法删除");
        }

        //查询当前分类是否关联了套餐，是，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.eq(Setmeal::getCategoryId,id);
        Integer count2 = setmealMapper.selectCount(lambdaQueryWrapper2);

        if(count2 > 0){
            //是，抛出一个业务异常
            throw new CustomException("分类关联了套餐，无法删除");
        }

        //正常删除分类
        categoryMappper.deleteById(id);
    }

    @Override
    public List<Category> myList(Category category) {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> categories = categoryMappper.selectList(lambdaQueryWrapper);
        return categories;

    }
}
