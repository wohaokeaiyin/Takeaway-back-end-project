package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;

import javax.servlet.http.HttpSession;

/**
 * @author SunQi
 * @date 2023/12/10 11:14
 */
public interface UserService extends IService<User> {
    R<String> myMsg(User user, HttpSession session);
}
