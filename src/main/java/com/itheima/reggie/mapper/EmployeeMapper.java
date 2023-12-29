package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author SunQi
 * @date 2023/11/28 21:33
 */

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
