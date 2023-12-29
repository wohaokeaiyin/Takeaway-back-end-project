package com.itheima.reggie.leecode;

/**
 * @author SunQi
 * @date 2023/12/8 21:04
 */

public class digitOneNumber {

    public int test1(int num){
        int temp;
        int x;
        int count=0;
        for(int i=0; i<=num; i++){
            temp = i;
            while(temp > 0){
                x = temp%10;
                if(x==1)count++;
                temp/=10;
            }
        }
        return count;
    }

    public int test2(int num){
        //例子：501222
        //固定某一位的数值，假设为1,判断其他位可能的变化状态
        int temp=num;
        int digit=1;
        int count=0;
        int high,low,cur;
        while(temp>0){
            high = num/digit/10;
            low = num%digit;
            cur = num/digit%10;
            temp/=10;
            if(cur < 1){
                //当是万位的时候，high=5,low=1222
                //所有可能的变化，0-4，0-9999，当high为5的时候，low任意数都会超出范围，因为这里的数组原来是是0，而不是1
                count += high*digit;
            }
            else if (cur == 1){
                //当是千位的时候，high=50,low=222
                //所有可能的变化，0-49，0-999和50，0-222
                count += high*digit+low+1;
            }
            else{
                //当是百位的时候，high=501,low=22
                //所有可能的变化，0-501，0-99正好，low的个数可以用digit表示
                count += ((high+1)*digit);
            }
            digit*=10;
        }
        return count;
    }

    public int test3(int k){
        int start=1;
        int dight=1;
        int count=9;
        while(k>count){
            k-=count;
            start *= 10;
            dight++;
            count = 9*dight*start;
        }
        int num = start + (k-1)/2;
        return Integer.toString(num).charAt((k-1)%dight)-'0';
    }

    public boolean articleMatch(String s, String p) {
        int len1 = s.length()+1;
        int len2 = p.length()+1;
        boolean[][] dp = new boolean[len1][len2];
        //s和p都是空字符串，定义为可以匹配成功
        dp[0][0]=true;
        //只有偶数位检查，因为像“*”或者“ab*”这样的*所在的奇数位，一定不会匹配一个空字符串，因为*作用的空间仅在于它紧挨着
        //前面一个字符
        for(int j=2; j<=len2; j+=2){
            dp[0][j] = dp[0][j-2]&&(p.charAt(j-1)=='*');
        }
        //下面修饰的dp的i、j实际对应的都是s和p字符串中的i-1、j-1
        for(int i=1; i<len1; i++){
            for(int j=1; j<len2;j++){
                //两种情况
                if (p.charAt(j - 1) == '*') {
                    //前者是舍弃*修饰的字符；后者是如果p的前j字符可以修饰s的前i-1个字符的话，s的最后一个字符是否也可以通过p的最后*的功能去匹配呢
                    dp[i][j] = dp[i][j-2] || (dp[i-1][j] && (s.charAt(i-1)==p.charAt(j-2) || p.charAt(j-2)=='.'));
                }
                //当字符是字母或者.的时候，比较简单，因为.可以和任意一个字符匹配
                else{
                    dp[i][j] = dp[i-1][j-1]&&(s.charAt(i-1)==p.charAt(j-1)||p.charAt(j-1)=='.');
                }
            }
        }
        return dp[len1-1][len2-1];
    }

    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int[] temp = new int[m];
        int i,j;
        for(i=0; i<m; i++){
            temp[i] = nums1[i];
        }
        i=j=0;
        int k=0;
        while(i<m && j<n){
            if(temp[i]<=nums2[j]){
                nums1[k++]=temp[i++];
            }
            else{
                nums1[k++]=temp[j++];
            }
        }
        while(i<m){
            nums1[k++]=temp[i++];
        }
        while(j<n){
            nums1[k++]=temp[j++];
        }
    }

    public int removeElement(int[] nums, int val) {
        int k=0;
        int i=0;
        for(i=0; i<nums.length; i++){
            if(nums[i] != val){
                nums[k++]=nums[i];
            }
        }
        return k--;
    }

    public int removeDuplicates(int[] nums) {
//        if(nums.length==0 || nums==null){
//            return 0;
//        }
//        int count=0;
//        int k=1;
//        for(int i=1; i<nums.length; i++){
//            if(nums[i]==nums[i-1])continue;
//            nums[k++]=nums[i];
//        }
//        return k--;
        if( nums.length == 0|| nums==null){
            return 0;
        }
        int k=0;
        int count=0;
        for(int i=0; i<nums.length; i++){
            if(i==0) {
                k++;
                count++;
                continue;
            }
            if(nums[i] == nums[k-1] && count>=2){
                count=0;
                continue;
            }
            nums[k++] = nums[i];
            count++;
        }
        return k--;
    }

    public int majorityElement(int[] nums) {
        int major = nums[0];
        int count=1;
        for(int i=1; i<nums.length; i++){
            if(nums[i]==major)count++;
            else{
                count--;
            }
            if(count==0){
                major=nums[i];
                count=1;
            }
        }
        return major;
    }
    public void rotate(int[] nums, int k) {
        int len = nums.length;
        int kTrue = k%len;
        reverse(nums,0,len-kTrue-1);
        reverse(nums,len-kTrue,len-1);
        reverse(nums,0,nums.length-1);
    }

    public void reverse(int[] nums,int left,int right){
        int mid = (left+right)/2;
        for(int i=0; i<=mid-left; i++){
            int temp = nums[i+left];
            nums[i+left]=nums[right-left+i];
            nums[right-left+i]=temp;
        }
    }

    public int maxProfit(int[] prices) {
//        int cost = prices[0];
//        int profit = Integer.MIN_VALUE;
//        for(int i=1; i<prices.length; i++){
//            cost = Math.min(cost,prices[i]);
//            profit = Math.max(profit,prices[i]-cost);
//        }
//        return profit;
        int len = prices.length;
        int[][] dp = new int[len][2];
        dp[0][0]=0;
        dp[0][1]=-prices[0];
        for(int i=1; i<len; i++){
            dp[i][0] = Math.max(dp[i-1][0],dp[i-1][1]+prices[i]);
            dp[i][1] = Math.max(dp[i-1][1],dp[i-1][0]-prices[i]);
        }
        return dp[len-1][0];
    }

}
