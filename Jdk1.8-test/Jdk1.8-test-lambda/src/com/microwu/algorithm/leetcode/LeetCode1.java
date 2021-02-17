package com.microwu.algorithm.leetcode;

import java.util.HashMap;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/7   14:02
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LeetCode1 {

    /**
     * 暴力破解
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/7  14:03
     *
     * @param   	nums
     * @param 		target
     * @return  int[]
     */
    public static int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * 使用哈希表降低时间复杂度
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/7  14:16
     *
     * @param   	nums
     * @param 		target
     * @return  int[]
     */
    public static int[] twoSum2(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[]{i, map.get(target - nums[i])};
            }
            map.put(nums[i], i);
        }
        return new int[0];
    }

    public static void main(String[] args) {

    }

}