package com.microwu.algorithm.leetcode;

import java.util.Arrays;

/**
 * Description: 移除元素
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/7   11:22
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LeetCode27 {

    public static int removeElement(int[] nums, int val) {
        // 1. 先找有几个 val
        int size = 0;
        for (int i = 0, j = 0; i < nums.length; i++) {
            if (nums[i] != val) {
                size++;
                nums[j++] = nums[i];
            }
        }

        // 2. 移动 size 个元素
//        int i = 0, j = 0;
//        while (j < size) {
//            if (nums[i] != val) {
//                nums[j++] = nums[i];
//            }
//            i++;
//        }
        return size;
    }

    /**
     * 当删除的元素很少时
     *
     * 1, 2, 3, 5, 4 val = 4
     * 存在前四个元素的不必要赋值
     *
     * [4, 1, 2, 3, 5] val = 4
     * 因为元素顺序可以更改，只需要与最后一个元素交换
     * 需要考虑最后一个元素是否也是 val
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/7  13:33
     *
     * @param   	nums
     * @param 		val
     * @return  int
     */
    public static int removeElement2(int[] nums, int val) {
//        int length = nums.length - 1;
//        int i = 0;
//        while (i <= length) {
//            while (nums[i] == val) {
//                // 如果相等的话，和最后一个元素交换
//                int temp = nums[length];
//                nums[length] = nums[i];
//                nums[i] = temp;
//                length--;
//            }
//            i++;
//        }
//        return nums.length - length - 1;
        int i = 0;
        int n = nums.length;
        while (i < n) {
            if (nums[i] == val) {
                nums[i] = nums[n - 1];
                // reduce array size by one
                n--;
            } else {
                i++;
            }
        }
        return n;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{0, 1, 2, 2, 3, 0, 4, 2};
        removeElement2(nums, 2);
        System.out.println(Arrays.toString(nums));
    }
}