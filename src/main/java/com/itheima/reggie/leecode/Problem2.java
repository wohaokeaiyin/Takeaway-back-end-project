package com.itheima.reggie.leecode;

/**
 * @author SunQi
 * @date 2023/12/6 22:08
 */

public class Problem2 {
    public int cuttingBamboo(int bamboo_len) {
        if(bamboo_len < 3)return bamboo_len-1;
        int a = bamboo_len/3;
        int b = bamboo_len%3;
        if(b==1)return (int) (Math.pow(3,a-1)*4);
        if(b==2)return (int)(Math.pow(3,a)*2);
        return (int)(Math.pow(3,a));
    }

    public int cuttingBamboo2(int bamboo_len) {
        int a = bamboo_len/3-1;
        int b = bamboo_len%3;
        int p = 1000000007;
        Long result = 1L;
        Long x = 3L;
        while(a > 0){
            if(a%2==1)result = (result*x)%p;
            x = (x*x)%p;
            a/=2;
        }
        if(b==1)return (int) ((result*4)%p);
        if(b==2)return (int) ((result*6)%p);
        return (int) ((result*3)%p);
    }
}
