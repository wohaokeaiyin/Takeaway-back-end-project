package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.itheima.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author SunQi
 * @date 2023/12/10 15:20
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
