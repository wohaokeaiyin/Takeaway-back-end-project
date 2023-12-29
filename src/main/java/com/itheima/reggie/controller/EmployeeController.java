package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author SunQi
 * @date 2023/11/28 21:40
 */
@Slf4j
@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        R result = employeeService.login(request,employee);
        return result;
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息:{}",employee.toString());

        //设置初试密码123456
        String s = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(s);
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        R<Page> result = employeeService.page(page,pageSize,name);
        return result;
    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){
        log.info(employee.toString());
        long id = Thread.currentThread().getId();
        log.info("线程id为：{}",id);
        R result = employeeService.myUpdate(request,employee);
        return result;
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工对象");
        R result = employeeService.myGetById(id);
        return result;
    }
}
