package com.xiao.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Hot100Demo {

    public static void main(String[] args) {
        // 输出 hello world
        System.out.println("hello world");

        // 输出 你好
        System.out.println("你好");

    }


    // https://leetcode.cn/problems/subarray-sum-equals-k/?envType=study-plan-v2&envId=top-100-liked
    public int subarraySum(int[] nums, int k) { // [1,2,1,2,1]
        int res = 0;

        for (int i = 0; i < nums.length; i ++) {
            if (nums[i] == k) {
                res ++;
                continue;
            }
            for (int j = i + 1; i < nums.length; j ++) {
                if (nums[i] + nums[j] == k) res ++;
            }
        }

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i ++) {
            map.put(nums[i], map.getOrDefault(nums[i], 0) + 1);
        }

        for (int i = 0; i < nums.length; i ++) {
            int num = map.get(nums[i]);
            if (nums[i] == k) {
                res += num;
            } else if (k - nums[i] == nums[i]) {
                Integer tmp = map.getOrDefault(k - nums[i], 0);
                if (tmp != 0) {
                    res += tmp - 1;
                }
            } else if (map.containsKey(k - nums[i])) {
                res += map.get(k - nums[i]) * num;
            }
            map.remove(nums[i]);
        }

        return res;
    }

    // https://leetcode.cn/problems/find-all-anagrams-in-a-string/?envType=study-plan-v2&envId=top-100-liked
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();

        int[] pArr = new int[26], sArr = new int[26];
        for (int i = 0; i < p.length(); i ++) {
            pArr[p.charAt(i) - 'a'] ++;
        }

        for (int i = 0; i < s.length(); i ++) {
            sArr[s.charAt(i) - 'a'] ++;
            int l = i - p.length() + 1;
            if (l < 0) continue;

            if (Arrays.equals(pArr, sArr)) {
                res.add(l);
            }
            sArr[s.charAt(l) - 'a'] --;
        }

        return res;

//        List<Integer> res = new ArrayList<>();
//
//        int[] pArr = new int[26];
//        for (int i = 0; i < p.length(); i ++) {
//            pArr[p.charAt(i) - 'a'] ++;
//        }
//
        //
//        for (int i = 0; i <= s.length() - p.length(); i ++) {
//            int[] sArr = new int[26];
//            sArr[s.charAt(i) - 'a'] ++;
//            if (i >= p.length()) sArr[s.charAt(i - p.length()) - 'a'] --;
//
//            if (Arrays.equals(pArr, sArr)) {
//                res.add(i);
//            }
//        }
//
//        return res;
    }

    // https://leetcode.cn/problems/longest-substring-without-repeating-characters/description/?envType=study-plan-v2&envId=top-100-liked
    public int lengthOfLongestSubstring(String s) {
        int res = 0;

        Set<Character> set = new HashSet<>();
        int index = 0;
        for (int i = 0; i < s.length(); i ++) {
            char c = s.charAt(i);
            while (set.contains(c)) {
                set.remove(s.charAt(index));
                index ++;
            }
            set.add(c);
            res = Math.max(res, set.size());
        }

        return res;
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


    // ------------ very day ------------
    public static int areaOfMaxDiagonal(int[][] dimensions) {
        int res = 0, maxArea = 0;
        double maxLen = 0f;

        for (int i = 0; i < dimensions.length; i ++) {
            int area = dimensions[i][0] * dimensions[i][1];
            double len = Math.sqrt(dimensions[i][0] * dimensions[i][0] + dimensions[i][1] * dimensions[i][1]);
            if (len > maxLen) {
                maxLen = len;
            } else if (len == maxLen) {
                maxArea = Math.max(maxArea, area);
            }
        }

        return res;
    }

}
