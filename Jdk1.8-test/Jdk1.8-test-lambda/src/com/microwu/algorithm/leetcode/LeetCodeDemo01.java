package com.microwu.algorithm.leetcode;

import java.util.Arrays;

/**
 * Description: 双指针系列
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/17   14:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LeetCodeDemo01 {

    /**
     * 在有序数组中找出两个数，使它们的和为 target。
     * <p>
     * Input: numbers={2, 7, 11, 15}, target=9
     * Output: index1=1, index2=2
     * <p>
     * 思路:
     * 使用双指针，一个指针指向值较小的元素，一个指针指向值较大的元素。
     * 指向较小元素的指针从头向尾遍历，指向较大元素的指针从尾向头遍历。
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/2/17  14:45
     */
    private static void test() {
        int[] nums = {1, 3, 5, 7, 11};
        int num = 1;
        int i = 0;
        int j = nums.length - 1;
        while (i < j) {
            int sum = nums[i] + nums[j];
            if (num == sum) {
                System.out.println("i = " + i + "; j = " + j);
                return;
            }
            if (sum > num) {
                j--;
            } else {
                i++;
            }

        }
        System.out.println("没有找到~~~");
    }

    /**
     * Input: 5
     * Output: True
     * Explanation: 1 * 1 + 2 * 2 = 5
     * <p>
     * 判断一个非负整数是否为两个整数的平方和。
     * <p>
     * 思路: 可以看成是在元素为 0~target 的有序数组中查找两个数，使得这两个数的平方和为 target，
     * 如果能找到，则返回 true，表示 target 是两个整数的平方和。
     * <p>
     * 这个最大数选择为 target 的开方
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/2/17  14:47
     */
    private static void test02() {
        int target = 40;
        int i = 0;
        int j = (int) Math.sqrt(target);
        while (i < j) {
            int sum = i * i + j * j;
            if (sum == target) {
                System.out.println(i + "," + j);
                return;
            } else if (sum < target) {
                i++;
            } else {
                j--;
            }
        }
        System.out.println("没有找到~~");

    }

    /**
     * 反转字符串
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/2/17  14:57
     */
    private static void test03() {

    }

    /**
     * Input: "abca"
     * Output: True
     * Explanation: You could delete the character 'c'.
     * 题目描述：可以删除一个字符，判断是否能构成回文字符串。
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/2/17  14:58
     */
    private static boolean test04() {
        String str = "abcdcbfva";
        int i = 0;
        int j = str.length() - 1;
        while (i < j) {
            if (str.charAt(i) != str.charAt(j)) {
                return isPalindrome(str, i + 1, j) ||
                        isPalindrome(str, i, j - 1);
            }
            i++;
            j--;
        }
        return true;

    }

    private static boolean isPalindrome(String str, int i, int j) {
        for (; i < j; i++, j--) {
            if (str.charAt(i) != str.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Input:
     * nums1 = [1,2,3,0,0,0], m = 3
     * nums2 = [2,5,6],       n = 3
     * <p>
     * Output: [1,2,2,3,5,6]
     * 题目描述：把归并结果存到第一个数组上。
     * <p>
     * 需要从尾开始遍历，否则在 nums1 上归并得到的值会覆盖还未进行归并比较的值。
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/2/18  10:16
     */
    public static void test05() {
        int[] nums1 = {1, 2, 7, 0, 0, 0};
        int[] nums2 = {2, 5, 6};
        int i = 2;
        int j = nums2.length - 1;
        int k = nums1.length - 1;

        while (i >= 0 && j >= 0) {
            if (nums2[j] > nums1[i]) {
                nums1[k--] = nums2[j--];
            } else {
                nums1[k--] = nums1[i--];
            }
        }

        System.out.println(Arrays.toString(nums1));

    }

    /**
     * 判断链表是否成环
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/2/18  10:44
     */
    private static void test06() {

    }

    /**
     * Input:
     * s = "abpcplea", d = ["ale","apple","monkey","plea"]
     * <p>
     * Output:
     * "apple"
     * 题目描述：删除 s 中的一些字符，使得它构成字符串列表 d 中的一个字符串，
     * 找出能构成的最长字符串。如果有多个相同长度的结果，返回字典序的最小字符串。
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/2/19  9:29
     */
    private static void test07(String s, String[] ds) {
        String longString = "";
        for (String d : ds) {
            int i = longString.length();
            int length = d.length();
            if (i > length || (i == length && d.compareTo(longString) > 0)) {
                continue;
            }
            if (isSubstr(s, d)) {
                longString = d;
            }
        }
        System.out.println(longString);
    }

    private static boolean isSubstr(String s, String str) {
        // s = "abpcplea", d = "ale"
        int i = 0;
        int j = 0;
        while (i < s.length() && j < str.length()) {
            if (s.charAt(i) == str.charAt(j)) {
                j++;
            }
            i++;
        }
        return j == str.length();
    }

    public static void main(String[] args) {
        String[] strings = {"ale","apple","monkey","plea"};
        test07("abpcplea", strings);
    }
}