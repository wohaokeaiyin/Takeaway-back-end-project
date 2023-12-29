package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;

import java.util.List;

/**
 * @author SunQi
 * @date 2023/12/1 19:47
 */

public interface CategoryService extends IService<Category> {
    R mySave(Category category);

    R<Page> myPage(int page, int pageSize);

    void myDelete(Long id);

    List<Category> myList(Category category);
}
