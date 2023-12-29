package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.Utils.ValidateCodeUtils;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.mapper.UserMapper;
import com.itheima.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * @author SunQi
 * @date 2023/12/10 11:14
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public R<String> myMsg(User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();

        //生成随机的验证码
        if(StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
            //调用阿里云提供的短信服务API完成短信发送
//            SMSUtils.sendMessage("瑞吉外卖","SMS_291965315",phone,code);
            //需要将生成的验证码保存到session
            session.setAttribute(phone,code);
            return R.success("手机验证码短信发送成功");
        }
        return R.error("短信发送失败");



    }
}
