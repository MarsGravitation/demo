package com.microwu.algorithm.leetcode;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Description: 给定两个数组，编写一个函数来计算它们的交集
 *  说明：
 *      1. 输出结果中每个元素出现的次数，英语元素在两个数组中出现次数的最小值一样
 *      2. 我们可以不考虑输出结果的顺序
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/22   12:50
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LeetCode350 {

    public static int[] intersect(int[] nums1, int[] nums2) {
        int size = nums1.length - nums2.length > 0 ? nums2.length : nums1.length;
        int[] ints = new int[size];

        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i : nums1) {
            if (!map.containsKey(i)) {
                // 如果不存在
                map.put(i, 1);
            } else {
                map.put(i, map.get(i) + 1);
            }
        }

        int index = 0;
        for (int i : nums2) {
            if (map.containsKey(i) && map.get(i) > 0) {
                ints[index++] = i;
                map.put(i, map.get(i) - 1);
            }
        }
        return ints;
    }

    public static int[] intersect2(int[] nums1, int[] nums2) {
        // 为了降低空间复杂度，遍历较短的数组并在哈希表中记录每个数字以及对应出现的此处
        // 然后遍历较长的数组得到交集
        int size = nums1.length > nums2.length ? nums2.length : nums1.length;
        int[] results = new int[size];

        int[] longArr = nums1.length > nums2.length ? nums1 : nums2;
        int[] shortArr = nums1.length > nums2.length ? nums2 : nums1;

        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i : shortArr) {
            if (map.containsKey(i)) {
                map.put(i, map.get(i) + 1);
            } else {
                map.put(i, 1);
            }
        }

        int index = 0;
        for (int i : longArr) {
            if (map.containsKey(i) && map.get(i) > 0) {
                results[index++] = i;
                map.put(i, map.get(i) - 1);
            }
        }

        int[] copy = new int[index];
        System.arraycopy(results, 0, copy, 0, index);
        return copy;
    }

    /**
     * 官方解
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/5  14:54
     *
     * @param   	nums1
     * @param 		nums2
     * @return  int[]
     */
    public static int[] intersect3(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return intersect3(nums2, nums1);
        }

        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : nums1) {
            int count = map.getOrDefault(num, 0) + 1;
            map.put(num, count);
        }

        int[] intersection = new int[nums1.length];
        int index = 0;
        for (int num : nums2) {
            int count = map.getOrDefault(num, 0);
            if (count > 0) {
                intersection[index++] = num;
                count--;
                if (count > 0) {
                    map.put(num, count);
                } else {
                    map.remove(num);
                }
            }
        }
        return Arrays.copyOfRange(intersection, 0, index);
    }

    /**
     * 如果两个数组是有序的
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/5  14:58
     *
     * @param   	nums1
     * @param 		nums2
     * @return  int[]
     */
    public static int[] intersect4(int[] nums1, int[] nums2) {
        // 只需要遍历较短的那个数组
        if (nums1.length > nums2.length) {
            return intersect4(nums2, nums1);
        }

        int size = nums1.length;
        int[] result = new int[size];

        // 定义两个指针，分别指向两个数组
        int a = 0, b = 0;
        int index = 0;
        while (a < size && b < nums2.length) {
            // 判断是否相等
            if (nums1[a] == nums2[b]) {
                result[index++] = nums1[a];
                a++;
                b++;
            } else {
                if (nums1[a] > nums2[b]) {
                    // 如果 a 大的话
                    b++;
                } else {
                    a++;
                }
            }
        }
        return Arrays.copyOfRange(result, 0, index);
    }

    /**
     * 官方解
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/5  15:11
     *
     * @param   	nums1
     * @param 		nums2
     * @return  int[]
     */
    public static int[] intersect5(int[] nums1, int[] nums2) {
        int length1 = nums1.length, length2 = nums2.length;
        int[] intersection = new int[Math.min(length1, length2)];
        int index1 = 0, index2 = 0, index3 = 0;
        while (index1 < length1 && index2 < length2) {
            if (nums1[index1] < nums2[index2]) {
                index1++;
            } else if (nums1[index1] > nums2[index2]) {
                index2++;
            } else {
                intersection[index3] = nums1[index1];
                index1++;
                index2++;
                index3++;
            }
        }
        return Arrays.copyOfRange(intersection, 0, index3);
    }

    public static void main(String[] args) {
//        int[] num1 = {4, 9, 5};
//        int[] num2 = {9, 4, 9, 8, 4};
        int[] num1 = {1, 2, 2, 4};
        int[] num2 = {2, 2};

        int[] intersect = intersect4(num1, num2);
        System.out.println(Arrays.toString(intersect));
    }

}