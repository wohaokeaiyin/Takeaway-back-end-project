package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.mapper.EmployeeMapper;
import com.itheima.reggie.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author SunQi
 * @date 2023/11/28 21:38
 */

@Service
public class EmployeeSerivceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService{

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public R login(HttpServletRequest request,Employee employee) {
        //讲页面提交的密码进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //根据页面提交的用户名查询数据库
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee employee1 = employeeMapper.selectOne(employeeLambdaQueryWrapper);

        //如果查询为null，返回查询失败
        if(employee1 == null){
            return R.error("登录失败");
        }

        //密码比对，如果不一致返回查询失败
        if(!employee1.getPassword().equals(password)){
            return R.error("查询失败");
        }

        //查看员工状态，如果已经禁用，则返回员工已禁用结果
        if(employee1.getStatus() == 0){
            return R.error("账号已禁用");
        }

        //登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",employee1.getId());
        return R.success(employee1);

    }

    @Override
    public R<Page> page(Integer page, Integer pageSize, String name) {
        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();

        //添加过滤条件
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //排序
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeMapper.selectPage(pageInfo,lambdaQueryWrapper);

        return R.success(pageInfo);
    }

    @Override
    public R<String> myUpdate(HttpServletRequest request, Employee employee) {
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);

        employeeMapper.updateById(employee);
        return R.success("员工信息修改成功");
    }

    @Override
    public R myGetById(Long id) {
        Employee employee = employeeMapper.selectById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到员工信息");

    }
}
