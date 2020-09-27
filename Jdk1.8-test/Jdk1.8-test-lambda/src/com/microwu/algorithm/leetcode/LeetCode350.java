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

    public static void main(String[] args) {
        int[] num1 = {1, 2, 2, 1};
        int[] num2 = {2, 2};

        int[] intersect = intersect(num1, num2);
        System.out.println(Arrays.toString(intersect));
    }

}