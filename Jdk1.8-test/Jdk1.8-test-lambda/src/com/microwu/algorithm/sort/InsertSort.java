package com.microwu.algorithm.sort;

import java.util.Arrays;

/**
 * Description: 插入排序
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/19   11:19
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class InsertSort {

    /**
     * 自己想的, 交换法
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/2/19  13:43
     */
    public static void test() {
        int[] ints = {9, 8, 7, 6, 5};
        for (int i = 1; i < ints.length; i++) {
            for (int j = i; j > 0; j--) {
                if (ints[j - 1] > ints[j]) {
                    SortUtils.swap(ints, j, j - 1);
                }
            }
        }
        System.out.println(Arrays.toString(ints));
    }

    /**
     * 临时变量法
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/2/19  13:44
     */
    private static void test02() {
        int[] nums = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        for (int i = 1, j; i < nums.length; i++) {
            int temp = nums[i];
            for (j = i; j > 0; j--) {
                if (nums[j - 1] > temp) {
                    nums[j] = nums[j - 1];
                } else {
                    break;
                }
            }
            nums[j] = temp;
        }
        System.out.println(Arrays.toString(nums));
    }

    private static void test03() {
        int[] nums = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        for (int i = 1; i < nums.length; i++) {
            int temp = nums[i];
            int leftIndex = i - 1;
            while (leftIndex >= 0 && nums[leftIndex] > temp) {
                nums[leftIndex + 1] = nums[leftIndex];
                leftIndex--;
            }
            nums[leftIndex + 1] = temp;

        }

        System.out.println(Arrays.toString(nums));
    }

    public static void main(String[] args) {
        test03();

    }
}