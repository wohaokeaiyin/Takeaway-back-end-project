package com.itheima.reggie.controller;

import com.itheima.reggie.service.OrderDetatilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author SunQi
 * @date 2023/12/12 14:44
 */
@RestController
@Slf4j
@RequestMapping("orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetatilService orderDetatilService;
}
