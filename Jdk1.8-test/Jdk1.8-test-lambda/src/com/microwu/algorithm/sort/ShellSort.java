package com.microwu.algorithm.sort;

import java.util.Arrays;

/**
 * Description: 希尔排序
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/19   11:32
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ShellSort {

    private static void test() {
        int[] nums = {9, 8, 7, 6, 5, 4, 3, 2, 1};

        for (int gap = nums.length / 2; gap > 0; gap /= 2) {
            // gap 组, 对每组进行插入排序
            for (int i = 0; i < gap; i++) {
                for (int j = i + gap; j < nums.length; j += gap) {
                    int temp = nums[j];
                    int k = j - gap;
                    while (k >= 0 && nums[k] > temp) {
                        nums[j] = nums[k];
                    }
                    nums[k] = temp;


                }

            }

        }
        System.out.println(Arrays.toString(nums));
    }

    private static void test02() {
        int[] a = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        // gap为步长，每次减为原来的一半。
        for (int gap = a.length / 2; gap > 0; gap /= 2) {

            // 共gap个组，对每一组都执行直接插入排序
            for (int i = 0; i < gap; i++) {

                for (int j = i + gap; j < a.length; j += gap) {

                    // 如果a[j] < a[j-gap]，则寻找a[j]位置，并将后面数据的位置都后移。
                    if (a[j] < a[j - gap]) {

                        int tmp = a[j];
                        int k = j - gap;
                        while (k >= 0 && a[k] > tmp) {
                            a[k + gap] = a[k];
                            k -= gap;
                        }
                        a[k + gap] = tmp;
                    }
                }
            }
        }

        System.out.println(Arrays.toString(a));
    }

    private static void test03() {
        int[] nums = {9, 8, 7, 6, 5, 4, 3, 2, 1};

        for (int gap = nums.length / 2; gap > 0; gap /= 2) {
            for (int i = 0; i < gap; i++) {
                for (int j = i + gap; j < nums.length; j += gap) {
                    int k = j - gap;
                    int temp = nums[j];
                    while (k >= 0 && nums[k] > temp) {
                        nums[j] = nums[k];
                        k -= gap;
                    }
                    nums[k + gap] = temp;
                }

            }

        }
        System.out.println(Arrays.toString(nums));
    }

    /**
     * 对每组进行插入排序
     *
     * @param args
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/2/19  14:10
     */
    public static void main(String[] args) {
        test03();

    }
}