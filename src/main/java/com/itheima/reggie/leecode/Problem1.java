package com.itheima.reggie.leecode;

/**
 * @author SunQi
 * @date 2023/12/6 21:14
 */

public class Problem1 {

    public int[] statisticalResult(int[] arrayA) {
//        int[] arrayB = new int[arrayA.length];
//        int sum=1;
//        for(int i=0; i<arrayA.length; i++){
//            sum *= arrayA[i];
//        }
//        for(int i=0; i<arrayB.length; i++){
//            arrayB[i]=sum/arrayA[i];
//        }
//        return arrayB;
        if(arrayA == null || arrayA.length==0){
            return null;
        }
        int len = arrayA.length;
        int[] left = new int[len];
        int[] right = new int[len];
        left[0] = right[len-1]=1;
        for(int i=1; i<len; i++){
            left[i] = left[i-1]*arrayA[i-1];
        }
        for(int i=len-2; i>=0; i--){
            right[i] = right[i+1]*arrayA[i+1];
        }

        int[] ans = new int[len];
        for(int i=0; i<len; i++){
            ans[i] = left[i]*right[i];
        }
        return ans;
    }
}
