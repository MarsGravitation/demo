package com.microwu.algorithm.sort;

import java.util.Arrays;

/**
 * Description: 快速排序
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/20   14:46
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class QuickSort {

    /**
     * 五分钟学算法 - 快排
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/2/26  15:49
     */
    private static void test() {
        int[] nums = new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1};

        quickSort(nums, 0, nums.length - 1);

        System.out.println(Arrays.toString(nums));

    }

    private static void quickSort(int[] nums, int left, int right) {
        if (left < right) {
            int partitionIndex = partition(nums, left, right);
            quickSort(nums, left, partitionIndex - 1);
            quickSort(nums, partitionIndex + 1, right);
        }
    }

    private static int partition(int[] nums, int left, int right) {
        int pivot = left;
        int index = pivot + 1;
        for (int i = index; i <= right; i++) {
            if (nums[i] < nums[pivot]) {
                SortUtils.swap(nums, i, index);
                index++;
            }
        }
        SortUtils.swap(nums, pivot, index - 1);
        return index - 1;
    }

    /**
     * 根据 五分钟算法 自己写的
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/2/26  15:50
     */
    public static void test02() {
        int[] nums = {3, 5, 8, 1, 2, 9, 4, 7, 6};
        test04(nums, 0, nums.length - 2, nums.length - 1);

        System.out.println(Arrays.toString(nums));
    }

    private static int test03(int[] nums, int left, int right, int p) {
        while (left < right) {
            if (nums[left] < nums[p]) {
                left++;
            } else {
                if (nums[right] < nums[p]) {
                    SortUtils.swap(nums, left, right);
                    left++;
                    right--;
                } else {
                    right--;
                }
            }
        }

        SortUtils.swap(nums, p, left);
        return left;
    }

    private static void test04(int[] nums, int left, int right, int p) {
        if (left <= right) {
            int i = test03(nums, left, right, p);
            test04(nums, left, i - 2, i - 1);
            test04(nums, i + 1, right - 1, right);
        }
    }

    private static void test05() {
        int[] nums = {3, 5, 8, 1, 2, 9, 4, 7, 6};

        test06(nums, 0, nums.length - 1);
        System.out.println(Arrays.toString(nums));
    }

    private static void test06(int[] nums, int left, int right) {
        int low = left;
        int height = right;
        if(left > right) {
            return;
        }
        int index = left;
        while (left < right) {
            while (left < right && nums[right] > nums[index]) {
                right--;
            }

            while (left < right && nums[left] < nums[index]) {
                left++;
            }

            SortUtils.swap(nums, left, right);

        }
        SortUtils.swap(nums, index, left);

        test06(nums, low, index - 1);
        test06(nums, index + 1, height);

    }

    /**
     * https://blog.csdn.net/qq_39404258/article/details/81806431
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/2/26  16:26
     *
     * @param   	a
//     * @param 		low
//     * @param 		height
     * @return  void
     */
    public static void sort(int[] a, int i, int j) {
        int low = i;
        int height = j;
        if (i > j) {//放在k之前，防止下标越界
            return;
        }
        int k = a[i];
        while (i < j) {
            while (i < j && a[j] > k) { //找出小的数
                j--;
            }
            while (i < j && a[i] <= k) {//找出大的数
                i++;
            }
            if (i < j) {//交换
                int swap = a[i];
                a[i] = a[j];
                a[j] = swap;
            }

        }
        //交换K
        k = a[i];
        a[i] = a[low];
        a[low] = k;

        //对左边进行排序,递归算法
        sort(a, low, i - 1);
        //对右边进行排序
        sort(a, i + 1, height);
    }

    public static void main(String[] args) {
        test05();
    }

}