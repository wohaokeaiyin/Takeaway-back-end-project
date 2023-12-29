package com.itheima.reggie.leecode;

import java.util.ArrayList;

/**
 * @author SunQi
 * @date 2023/12/7 14:23
 */

public class Problem3 {
    public int[][] fileCombination(int target) {
        ArrayList<int[]> res = new ArrayList<>();
        int i =1,j=2,s=3;
        while(i<j){
            if(s == target){
                int[] ans = new int[j - i + 1];
                for(int k=i;k<j;k++){
                    ans[k-i]=k;
                }
                res.add(ans);
            }
            if(s>=target){
                s-=i;
                i++;
            }
            else{
                j++;
                s+=j;
            }
        }
        return res.toArray(new int[0][]);
    }

    public boolean canJump(int[] nums) {
        //贪心算法
        int remote = 0;
        int i=0;
        while(i<=remote){
            remote = Math.max(remote,i+nums[i]);
            if(remote >= nums.length-1)return true;
            i++;
        }
        return false;
    }
}
