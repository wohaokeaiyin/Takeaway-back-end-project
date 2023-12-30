package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author SunQi
 * @date 2023/12/10 11:15
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        R<String> result = userService.myMsg(user,session);

        return result;
    }
    
    @PostMapping("login")
    public R<User> login(@RequestBody Map map,HttpSession session){
        log.info(map.toString());

        String phone = map.get("phone").toString();

        String code = map.get("code").toString();

        //从session中获取保存的验证码
        //Object codeInsession =  session.getAttribute(phone);

        //从redis中获取缓存的验证码
        Object codeInsession = redisTemplate.opsForValue().get(phone);
        if(codeInsession != null && codeInsession.equals(code)){
            //判断当前用户是否是新用户
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(userLambdaQueryWrapper);
            if(user == null){
                User usertemp = new User();
                usertemp.setPhone(phone);
                usertemp.setStatus(1);
                userService.save(usertemp);
            }
            session.setAttribute("user", user.getId());

            //如果用户登录成功，删除redis中缓存的验证码
            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登录失败");

    }
}
