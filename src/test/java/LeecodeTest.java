/**
 * @author SunQi
 * @date 2023/12/9 15:24
 */

public class LeecodeTest {


    public static void main(String[] args) {
        int[] nums = {1,1,1,2,2,3};
        int res = removeDuplicates(nums);
        System.out.println(res);
    }

    public static int removeDuplicates(int[] nums) {
//        if( nums.length == 0|| nums==null){
//            return 0;
//        }
//        int k=0;
//        int count=0;
//        for(int i=0; i<nums.length; i++){
//            if(i==0) {
//                k++;
//                count++;
//                continue;
//            }
//            if(nums[i] == nums[k-1] && count>=2){
//                count=0;
//                continue;
//            }
//            if(nums[i] != nums[k-1] && count==2){
//                count=0;
//            }
//            nums[k++] = nums[i];
//            count++;
//        }
//        return k--;
        int len = nums.length;
        if(len <= 2){
            return len;
        }
        int fast = 2,slow=2;
        while(fast<len){
            if(nums[slow-2] != nums[fast]){
                nums[slow]=nums[fast];
                slow++;
            }
            fast++;
        }
        return slow--;
    }
}
