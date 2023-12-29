package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SunQi
 * @date 2023/12/1 19:47
 */
@RestController
@Slf4j
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        R result = categoryService.mySave(category);
        return result;
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public R<Page> page(int page,int pageSize){
        R<Page> pageR = categoryService.myPage(page, pageSize);
        return pageR;
    }

    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类，id:{}",id);
//        categoryService.removeById(id);
        categoryService.myDelete(id);
        return R.success("分类删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("更新分类信息：{}",category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("list")
    public R<List<Category>> list(Category category){
        List<Category> list= categoryService.myList(category);
        return R.success(list);

    }



}
