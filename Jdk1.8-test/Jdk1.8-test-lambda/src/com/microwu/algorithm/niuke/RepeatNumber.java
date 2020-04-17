package com.microwu.algorithm.niuke;

/**
 * Description:
 *  在一个长度为 n 的数组里的所有数字都在 0 到 n-1 的范围内。
 *  数组中某些数字是重复的，但不知道有几个数字是重复的，
 *  也不知道每个数字重复几次。请找出数组中任意一个重复的数字。
 *
 *  Input:
 *  {2, 3, 1, 0, 2, 5}
 *
 * Output:
 *  2
 *
 *  要求: 时间复杂度O(n), 空间复杂度O(1)
 *
 *  不考虑时间复杂度，空间复杂度的想法：
 *      1. 双循环判断
 *      2. 排序
 *      3. HashSet
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/17   9:33
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RepeatNumber {

    /**
     * 时间复杂度O(n^)
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/2/17  9:55
     *
     * @param
     * @return  void
     */
    public static void test() {
        int[] nums = {4, 3, 1, 0, 0, 5};
        for(int i = 0; i < nums.length - 1; i++) {
            for(int j = i + 1; j < nums.length; j++) {
                if(nums[i] == nums[j]) {
                    System.out.println(nums[i]);
                    return;
                }
            }

        }
    }

    /**
     * 基数排序
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/2/17  10:59
     *
     * @param
     * @return  void
     */
    public static void test02() {
        int[] nums = {2, 3, 1, 0, 2, 5};
        for(int i = 0; i < nums.length; i++) {
            while(nums[i] != i) {
                if(nums[i] == nums[nums[i]]) {
                    System.out.println(nums[i]);
                    return;
                }
                swap(nums, i, nums[i]);
            }

        }
        System.out.println("没有重复数字~~~");
    }

    private static void swap(int[] nums, int i, int j) {
        int n = nums[i];
        nums[i] = nums[j];
        nums[j] = n;
    }

    public static void main(String[] args) {
        test02();
    }


}