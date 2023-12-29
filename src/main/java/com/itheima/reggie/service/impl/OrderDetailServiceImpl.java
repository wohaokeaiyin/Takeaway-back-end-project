package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.mapper.OrderDetatilMapper;
import com.itheima.reggie.service.OrderDetatilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author SunQi
 * @date 2023/12/12 14:42
 */
@Service
@Slf4j
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetatilMapper, OrderDetail> implements OrderDetatilService {
}
