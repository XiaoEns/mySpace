package com.xiao.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hot100Demo {

    public static void main(String[] args) {


    }


    public int countSquares(int[][] matrix) {
        int res = 0;

        return res;
    }

    // https://leetcode.cn/problems/trapping-rain-water/?envType=study-plan-v2&envId=top-100-liked
    public static int trap(int[] height) {

        return 0;
    }


    // https://leetcode.cn/problems/3sum/description/?envType=study-plan-v2&envId=top-100-liked
    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();

        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            if (nums[i] + nums[i + 1] + nums[i + 2] > 0) break;
            if (nums[i] + nums[nums.length - 2] + nums[nums.length - 1] < 0) continue;

            int l = i + 1, r = nums.length - 1;
            while (l < r) {
                int sum = nums[i] + nums[l] + nums[r];
                if (sum < 0) l ++;
                else if (sum > 0) r --;
                else {
                    result.add(Arrays.asList(nums[i], nums[l], nums[r]));
                    while (l < r && nums[l] == nums[l + 1]) l ++;
                    while (l < r && nums[r] == nums[r - 1]) r --;
                    l ++;
                    r --;
                }
            }
        }

        return result;
    }
    /**
     -1,0,1,2,-1,-4

     sort: -4, -1, -1, 0, 1, 2

     -1, -1, 2
     -1, 0, 1

     -----------
     -2,0,1,1,2

     sort: -2, 0, 1, 1, 2

     -2, 0, 2
     -2, 1, 1

     ----------
     2,-3,0,-2,-5,-5,-4,1,2,-2,2,0,2,-4,5,5,-10

     sort: -10, -5, -5, -4, -4, -3, -2, -2, 0, 0, 1, 2, 2, 2, 2, 5, 5

     -4,2,2
     */


}
