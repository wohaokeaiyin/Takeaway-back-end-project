package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;

import javax.servlet.http.HttpServletRequest;

/**
 * @author SunQi
 * @date 2023/11/28 21:36
 */

public interface EmployeeService extends IService<Employee> {
    R login(HttpServletRequest request, Employee employee);

    R<Page> page(Integer page, Integer pageSize, String name);

    R<String> myUpdate(HttpServletRequest request, Employee employee);

    R myGetById(Long id);
}
