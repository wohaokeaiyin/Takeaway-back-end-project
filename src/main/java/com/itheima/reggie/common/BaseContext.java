package com.itheima.reggie.common;

/**
 * 基于TreadLocal封装工具类
 * @author SunQi
 * @date 2023/12/1 16:27
 */

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
