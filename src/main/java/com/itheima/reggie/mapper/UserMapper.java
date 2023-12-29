package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author SunQi
 * @date 2023/12/10 11:13
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
