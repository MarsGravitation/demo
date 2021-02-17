package com.microwu.algorithm.leetcode;

import java.util.Arrays;

/**
 * Description: 旋转数组
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/7   9:32
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LeetCode189 {

    public static void rotate(int[] nums, int k) {
//        if (nums == null || nums.length == 0) {
//            return;
//        }
//        if (k <= 0) {
//            return;
//        }
//        int cur, next;
//        for (int i = 0; i < nums.length; i++) {
//            int n = (i + k) % nums.length;
//            next = nums[n];
//            nums[n] = cur;
//            cur = next;
//
//        }
//        System.out.println(Arrays.toString(nums));
    }

    /**
     * 暴力破解
     *  旋转 k 次，每次将数组旋转 1 个元素
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/7  10:22
     *
     * @param   	nums
     * @param 		k
     * @return  void
     */
    public static void rotate2(int[] nums, int k) {
        int temp, previous;
        for (int i = 0; i < k; i++) {
            previous = nums[nums.length - 1];
            for (int j = 0; j < nums.length; j++) {
                temp = nums[j];
                nums[j] = previous;
                previous = temp;
            }
        }
        System.out.println(Arrays.toString(nums));
    }

    /**
     * 使用额外的数组
     *  用一个额外的数组把每个元素放到正确的位置上，然后把新的数组拷贝到原数组中
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/7  10:37
     *
     * @param   	nums
     * @param 		k
     * @return  void
     */
    public static void rotate3(int[] nums, int k) {
        int[] newArr = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            newArr[(k + i) % nums.length] = nums[i];
        }
        System.out.println(Arrays.toString(newArr));
    }

    /**
     * 环状替换
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/7  10:50
     *
     * @param   	nums
     * @param 		k
     * @return  void
     */
    public static void rotate4(int[] nums, int k) {
//        int i = 0;
//        while (i++ % k == 0) {
//            int j = 0;
//            while ()
//        }
    }

    /**
     * 反转数组
     *  |- 反转所有数字
     *  |- 反转前 k 个数字
     *  |- 反转 n-k 个 数字
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/7  10:58
     *
     * @param   	nums
     * @param 		k
     * @return  void
     */
    public static void rotate5(int[] nums, int k) {
        // 反转所有数字
        reverse(nums, 0, nums.length - 1);

        // 反转前 k 个数字
        reverse(nums, 0, k - 1);

        // 反转后 n - k 个数字
        reverse(nums, k, nums.length - 1);
        System.out.println(Arrays.toString(nums));
    }

    private static void reverse(int[] nums, int start, int end) {
        int temp;
        while (start < end){
            temp = nums[end];
            nums[end] = nums[start];
            nums[start] = temp;
            start++;
            end--;
        }
    }


    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 3, 4, 5, 6, 7};
        rotate5(nums, 3);
    }
}