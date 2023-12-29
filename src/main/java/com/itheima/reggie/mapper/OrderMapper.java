package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author SunQi
 * @date 2023/12/12 14:34
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
